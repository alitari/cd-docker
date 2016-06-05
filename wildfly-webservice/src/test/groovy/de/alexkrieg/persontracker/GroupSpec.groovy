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

class GroupSpec extends Specification {

    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GroupSpec.class);

    @Shared url = "http://${System.properties.'wshost'}:${System.properties.'wsport'}/${System.properties.'wsname'}/"
    @Shared restClient = new RESTClient( url+"rest/" )
    @Shared soapClient = new SOAPClient(url +"MemberService?wsdl")

    @Shared member ='myMember@test.de'
    @Shared memberPassword ='mysecret'

    def authToken
    def authId


    def setupSpec() {
        logger.info 'using rest url:'+ restClient.getUrl()
    }

    def setup() {
        soapClient.send(deleteMemberBody(member))
        soapClient.send(addMemberBody(member, memberPassword))
        def loginResp = restClient.post(path: 'login' ) {
            json email:member, password:memberPassword
        }
        authToken = loginResp.json.authToken
        authId = loginResp.json.authId

        logger.info "logged in member: ${member} with authId/authToken: ${authId}/${authToken}"
    }


    def 'create group successful'() {
        given: 'no group the same name'
        restClient.post(path: 'group/delete/mygroup', headers:['auth-id':authId, 'auth-token':authToken])


        when: 'we attempt to create a new group'
        def resp = restClient.post(path: 'group/create/mygroup', headers:['auth-id':authId, 'auth-token':authToken])

        then: 'we should get a ok message and a the group name'
        assert resp.statusCode == HttpStatus.SC_OK
        assert resp.json.name == 'mygroup'
        assert resp.json.message == 'OK'
    }

    def 'create group failure beacause group already exists'() {
        given: 'a group the same name'
        restClient.post(path: 'group/create/mygroup', headers:['auth-id':authId, 'auth-token':authToken])


        when: 'we attempt to create the group again'
        def resp = restClient.post(path: 'group/create/mygroup', headers:['auth-id':authId, 'auth-token':authToken])

        then: 'we should get an error message explaining the reason'
        assert resp.statusCode == HttpStatus.SC_OK
        assert resp.json.name == 'mygroup'
        assert resp.json.message == 'Group already existed.'
    }

    def 'delete group successful'() {
        given: 'a group the same name'
        restClient.post(path: 'group/create/mygroup', headers:['auth-id':authId, 'auth-token':authToken])


        when: 'we attempt to delete the group'
        def resp = restClient.post(path: 'group/delete/mygroup', headers:['auth-id':authId, 'auth-token':authToken])

        then: 'we should get a ok message and a the group name'
        assert resp.statusCode == HttpStatus.SC_OK
        assert resp.json.name == 'mygroup'
        assert resp.json.message == 'OK'
    }

    def 'delete group failed because group does not exist'() {
        given: 'no group the same name'
        restClient.post(path: 'group/delete/mygroup', headers:['auth-id':authId, 'auth-token':authToken])


        when: 'we attempt to delete a non existing group'
        def resp = restClient.post(path: 'group/delete/mygroup', headers:['auth-id':authId, 'auth-token':authToken])

        then: 'we should get an error message and a the group name'
        assert resp.statusCode == HttpStatus.SC_OK
        assert resp.json.name == 'mygroup'
        assert resp.json.message == 'Group does not exist.'
    }

    def 'add member to group successful'() {
        given: 'a group the same name'
        restClient.post(path: 'group/create/mygroup', headers:['auth-id':authId, 'auth-token':authToken])

        when: 'we attempt to add the logged in member to the group'
        def resp = restClient.post(path: 'group/add/mygroup', headers:['auth-id':authId, 'auth-token':authToken]) {
            type 'application/json'
            text member
        }

        then: 'we should get a OK message and a the group name and member email'
        assert resp.statusCode == HttpStatus.SC_OK
        assert resp.json.name == 'mygroup'
        assert resp.json.message == 'OK'
        assert resp.json.memberEmails[0] == member
    }

    def 'add member to group failed because group does not exist'() {
        given: 'no group the same name'
        restClient.post(path: 'group/delete/mygroup', headers:['auth-id':authId, 'auth-token':authToken])

        when: 'we attempt to add the logged in member to the group'
        def resp = restClient.post(path: 'group/add/mygroup', headers:['auth-id':authId, 'auth-token':authToken]) {
            type 'application/json'
            text member
        }

        then: 'we should get an error message with the reason and a the group name and member email'
        assert resp.statusCode == HttpStatus.SC_OK
        assert resp.json.name == 'mygroup'
        assert resp.json.message == 'Group does not exist.'
        assert resp.json.memberEmails[0] == member
    }

    def 'add member to group failed because member does not exist'() {
        given: 'a group with the same name'
        restClient.post(path: 'group/create/mygroup', headers:['auth-id':authId, 'auth-token':authToken])

        and: 'no member with email "unknown@member.de" '
        soapClient.send(deleteMemberBody('unknown@member.de'))

        when: 'we attempt to add the not existing  member to the group '
        def resp = restClient.post(path: 'group/add/mygroup', headers:['auth-id':authId, 'auth-token':authToken]) {
            type 'application/json'
            text 'unknown@member.de'
        }

        then: 'we should get an error message with the reason '
        assert resp.statusCode == HttpStatus.SC_OK
        assert resp.json.name == 'mygroup'
        assert resp.json.message == 'Member does not exist.'
        assert resp.json.memberEmails[0] == 'unknown@member.de'
    }

    def 'add member to group failed because member is not himself'() {
        given: 'a group with the same name'
        restClient.post(path: 'group/create/mygroup', headers:['auth-id':authId, 'auth-token':authToken])

        and: 'an additional logged in member with email "foreigner@test.de"'
        soapClient.send(addMemberBody('foreigner@test.de',"foreigner"))
        def loginResp = restClient.post(path: 'login' ) {
            json email:'foreigner@test.de', password:'foreigner'
        }
        assert loginResp.json.message == 'OK'

        when: 'we attempt to add a member who is not himself'
        def resp = restClient.post(path: 'group/add/mygroup', headers:['auth-id':authId, 'auth-token':authToken]) {
            type 'application/json'
            text 'foreigner@test.de'
        }

        then: 'we should get an error message with the reason and a the group name and member email'
        assert resp.statusCode == HttpStatus.SC_OK
        assert resp.json.name == 'mygroup'
        assert resp.json.message == 'You only can add yourself to a group.'
        assert resp.json.memberEmails[0] == 'foreigner@test.de'
    }
    
    def 'show members of an empty group'() {
        given: 'a new created group with the same name'
        restClient.post(path: 'group/delete/mygroup', headers:['auth-id':authId, 'auth-token':authToken])
        restClient.post(path: 'group/create/mygroup', headers:['auth-id':authId, 'auth-token':authToken])

        when: 'we attempt to show all members of the group'
        def resp = restClient.get(path: 'group/members/mygroup', headers:['auth-id':authId, 'auth-token':authToken]) 

        then: 'we should get a  OK message with an empty list of members'
        assert resp.statusCode == HttpStatus.SC_OK
        assert resp.json.name == 'mygroup'
        assert resp.json.message == 'OK'
        assert resp.json.memberEmails == []
    }
    
    def 'show members of an group with 2 members'() {
        given: 'a group with the same name which contains the current logged in member'
        restClient.post(path: 'group/create/mygroup', headers:['auth-id':authId, 'auth-token':authToken])
        
        restClient.post(path: 'group/add/mygroup', headers:['auth-id':authId, 'auth-token':authToken]) {
            type 'application/json'
            text member
        }
        
        and: 'a member "groupmember@test.de" added to the group'
        soapClient.send(addMemberBody('groupmember@test.de',"member111"))
        def loginResp1 = restClient.post(path: 'login' ) {
            json email:'groupmember@test.de', password:'member111'
        }
        assert loginResp1.json.message == 'OK'
        
        restClient.post(path: 'group/add/mygroup', headers:['auth-id':loginResp1.json.authId, 'auth-token':loginResp1.json.authToken]) {
            type 'application/json'
            text 'groupmember@test.de'
        }

        when: 'we attempt to show all members of the group'
        def resp = restClient.get(path: 'group/members/mygroup', headers:['auth-id':authId, 'auth-token':authToken])

        then: 'we should get a  OK message with a list of the 2 members'
        assert resp.statusCode == HttpStatus.SC_OK
        assert resp.json.name == 'mygroup'
        assert resp.json.message == 'OK'
        assert member in resp.json.memberEmails
        assert 'groupmember@test.de' in resp.json.memberEmails
    }

   
}