/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.alexkrieg.persontracker

import wslite.rest.*

import spock.lang.*
import wslite.soap.*
import static AddDeleteMemberSpec.addMemberBody;
import static AddDeleteMemberSpec.deleteMemberBody
import org.apache.http.HttpStatus

class LoginSpec extends Specification {

    
    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LoginSpec.class);

    @Shared url = "http://${System.properties.'wshost'}:${System.properties.'wsport'}/${System.properties.'wsname'}/"
    @Shared restClient = new RESTClient( url+"rest/" )
    @Shared soapClient = new SOAPClient(url +"MemberService?wsdl")


    def setupSpec() {
        logger.info 'using rest url:'+ restClient.getUrl()
    }

    def setup() {
    }


    def 'login successful'() {
        given: 'a member with the same name'
        soapClient.send(addMemberBody('loginjames@mail.com', 'loginpass'))

        when: 'we attempt to login'
        def resp = restClient.post(path: 'login' ) {
            json email:'loginjames@mail.com', password:'loginpass'
        }

        then: 'we should get a authentication token'
        assert resp.statusCode == HttpStatus.SC_OK
        assert resp.json.authToken.size() == 36
        assert resp.json.message == 'OK'
        assert resp.json.authId == 'loginjames@mail.com'
    }

    def 'login failed because member does not exist'() {
        given: 'no member with the same name'
        soapClient.send(deleteMemberBody('loginjames@mail.com'))

        when: 'we attempt to login'
        def resp = restClient.post(path: "login" ) {
            json email:'loginjames@mail.com', password:'loginpass'
        }

        then: 'we should get an error message explaining the reason and no authentication token'
        assert resp.statusCode == HttpStatus.SC_OK
        assert !resp.json.authToken
        assert resp.json.message == 'Unknown member loginjames@mail.com'
        assert resp.json.authId == 'loginjames@mail.com'
    }

    def 'login failed due to wrong password'() {
        given: 'a member with the same name'
        soapClient.send(addMemberBody('loginjames@mail.com', 'loginpass'))

        when: 'we attempt to login with a wrong password'
        def resp = restClient.post(path: 'login' ) {
            json email:'loginjames@mail.com', password:'wrongpassword'
        }

        then: 'we should get an error message explaining the reason and no authentication token'
        assert resp.statusCode == HttpStatus.SC_OK
        assert !resp.json.authToken
        assert resp.json.message == 'Unknown password for loginjames@mail.com'
        assert resp.json.authId == 'loginjames@mail.com'
    }

    def 'logout successful'() {
        given: 'a logged in member with the same name '
        soapClient.send(addMemberBody('loginjames@mail.com', 'loginpass'))
        def loginResp = restClient.post(path: 'login' ) {
            json email:'loginjames@mail.com', password:'loginpass'
        }

        when: 'we attempt to logout '
        def resp = restClient.post(path: 'logout',headers:['auth-id':loginResp.json.authId, 'auth-token':loginResp.json.authToken] ) {
            type 'application/json'
            text 'loginjames@mail.com'
        }

        then: 'we should get an message ok '
        assert resp.statusCode == HttpStatus.SC_OK
        assert !resp.json.authToken
        assert resp.json.message == 'OK'
        assert resp.json.authId == 'loginjames@mail.com'
        
    }

    def 'after successful logout all further requests using the authToken get status "Unauthorized" '() {
        given: 'a member with the same name who is not logged in '
        soapClient.send(addMemberBody('loginjames@mail.com', 'loginpass'))
        def loginResp = restClient.post(path: 'login' ) {
            json email:'loginjames@mail.com', password:'loginpass'
        }
        def logoutResp = restClient.post(path: 'logout',headers:['auth-id':loginResp.json.authId, 'auth-token':loginResp.json.authToken] ) {
            type 'application/json'
            text 'loginjames@mail.com'
        }

        when: 'we attempt to logout (again) '
        def resp = restClient.post(path: 'logout',headers:['auth-id':loginResp.json.authId, 'auth-token':loginResp.json.authToken] ) {
            type 'application/json'
            text 'loginjames@mail.com'
        }

        then: 'an "Unauthorized"-Exception occurs'
        RESTClientException unauthorizedEx = thrown()
        assert unauthorizedEx.message == '401 Unauthorized'
    }
    
}