import time
from locust import FastHttpUser, task, between, events
import json
import os
import datetime
import pytz
from influxdb import InfluxDBClient
import socket
from locust_plugins.csvreader import CSVReader
import requests
requests.packages.urllib3.disable_warnings() 

first_name = CSVReader("first_names.csv")


hostname = socket.gethostname()
client = InfluxDBClient(
    ssl = True,
    verify_ssl = False,
    host = os.environ.get('INFLUXDB_HOST'),
    port = os.environ.get('INFLUXDB_PORT'), 
    username = os.environ.get('INFLUXDB_USERNAME'), 
    password = os.environ.get('INFLUXDB_PASSWORD'))
client.switch_database(os.environ.get('INFLUXDB_DATABASE'))





@events.request.add_listener
def my_request_handler(request_type, name, response_time, response_length, response,
                       context, exception, start_time, url, **kwargs):
    if exception:
        FAIL_TEMPLATE = '[{"measurement": "%s","tags": {"hostname":"%s","requestName": "%s","requestType": "%s","exception":"%s","status":"%s", "systemUnderTest":"%s", "testEnvironment":"%s"' \
                        '},"time":"%s","fields": {"responseTime": %i,"responseLength":"%s"}' \
                        '}]'
        json_string = FAIL_TEMPLATE % (
        "failures", hostname, name, request_type, exception, "fail", context['system_under_test'], context['test_environment'], datetime.datetime.now(tz=pytz.UTC),
        response_time, response_length)
        client.write_points(json.loads(json_string), time_precision='n')
    else:
        SUCCESS_TEMPLATE = '[{"measurement": "%s","tags": {"hostname":"%s","requestName": "%s","requestType": "%s","status":"%s", "systemUnderTest":"%s", "testEnvironment":"%s"' \
                        '},"time":"%s","fields": {"responseTime": %i, "responseLength":"%s"}' \
                        '}]'
        json_string = SUCCESS_TEMPLATE % (
        "requests", hostname, name, request_type, "success", context['system_under_test'], context['test_environment'], datetime.datetime.now(tz=pytz.UTC), response_time,
        response_length)
        client.write_points(json.loads(json_string), time_precision='n')

@events.init_command_line_parser.add_listener
def _(parser):
    parser.add_argument("--test-run-id", type=str, env_var="TEST_RUN_ID", default="", help="Test run id")
    parser.add_argument("--system-under-test", type=str, env_var="SYSTEM_UNDER_TEST", default="", help="System under test")
    parser.add_argument("--test-environment", type=str, env_var="TEST_ENVIRONMENT", default="", help="Test environment")
    parser.add_argument("--influxdb-host", type=str, env_var="INFLUXDB_HOST", default="", help="InfluxDb host")
    parser.add_argument("--influxdb-port", type=str, env_var="INFLUXDB_PORT", default="", help="InfluxDb port")
    parser.add_argument("--influxdb-username", type=str, env_var="INFLUXDB_USERNAME", default="", help="InfluxDb username")
    parser.add_argument("--influxdb-password", type=str, env_var="INFLUXDB_PASSWORD", default="", help="InfluxDb password")
    parser.add_argument("--influxdb-database", type=str, env_var="INFLUXDB_DATABAsE", default="", help="InfluxDb database")


class MyTaskSet(FastHttpUser):

    def context(self):
        return{
             'system_under_test': self.environment.parsed_options.system_under_test,
             'test_environment': self.environment.parsed_options.test_environment,
        }
    
   

   
  
    @task
    def get_remote_call_many(self):
        response = self.client.get("/remote/call-many?count=3&path=delay", 
        name="remote_call_async",
        headers={
            'perfana-request-name': 'remote_call_async',
            'perfana-test-run-id': self.environment.parsed_options.test_run_id
        })


    
    @task
    def get_flaky(self):
        response = self.client.get("/flaky?maxRandomDelay=240&flakiness=5",
        name="flaky_call",
        headers={
            'perfana-request-name': 'flaky_call',
            'perfana-test-run-id': self.environment.parsed_options.test_run_id
        })



    @task
    def get_delay(self):
        response = self.client.get("/delay?duration=200", 
        name="simple_delay",
        headers={
            'perfana-request-name': 'simple_delay',
            'perfana-test-run-id': self.environment.parsed_options.test_run_id
        })
    
    @task
    def get_db_employee_find_by_name(self):
        first_name = next(first_name)
        response = self.client.get(f"/remote/call-many?count=1&path=/db/employee/find-by-name?firstName={first_name[0]}", 
        name="database_call",
        headers={
            'perfana-request-name': 'database_call',
            'perfana-test-run-id': self.environment.parsed_options.test_run_id
        })

    @task
    def get_cpu_magic_identity_check(self):
        response = self.client.get("/cpu/magic-identity-check?matrixSize=133", 
        name="simple_cpu_burn",
        headers={
            'perfana-request-name': 'simple_cpu_burn',
            'perfana-test-run-id': self.environment.parsed_options.test_run_id
        })


class MyLocust(FastHttpUser):
    tasks = [MyTaskSet]
    wait_time = between(2, 5)
    host = "http://localhost:8080"

