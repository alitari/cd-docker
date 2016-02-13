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

import groovyx.net.http.RESTClient
import spock.lang.*

import spock.lang.Specification
import wslite.soap.*

class AddDeleteMemberSpec extends Specification {

    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AddDeleteMemberSpec.class);

    @Shared uri = "http://${System.properties.'wshost'}:${System.properties.'wsport'}//${System.properties.'wsname'}/"
    @Shared soapClient = new SOAPClient(uri +"MemberService?wsdl")


    def setupSpec() {
        logger.info 'using soap url:'+ soapClient.getServiceURL();
    }

    def 'add member with success'() {
        given: "no member with same name"
        soapClient.send(deleteMemberBody("myMember@test.de"))

        when: "we attempt to add a member"
        def resp = soapClient.send(addMemberBody("myMember@test.de","myPassword"))

        then: "we should get a response without an error message"
        assert !resp.hasFault()
        assert  "myMember@test.de" == resp.addMemberResponse.AddedMember.email.toString()
    }

    def 'add member failure because member already exists'() {
        given: "a member with same name"
        soapClient.send(addMemberBody("myMember@test.de","myPassword"))

        when: "we attempt to add the member again"
        def resp = soapClient.send(addMemberBody("myMember@test.de","myPassword"))

        then: "we should get a response with an error message explaining the reason"
        assert !resp.hasFault()
        assert  'Member with email="myMember@test.de" already exists' == resp.addMemberResponse.AddedMember.errorMessage.toString()
    }

    def 'delete member with success'() {
        given: "an existing member with the same name"
        soapClient.send(addMemberBody("myAddedMember@test.de","mySuperpass"))

        when: "we attempt to delete the member"
        def resp = soapClient.send(deleteMemberBody("myAddedMember@test.de"))

        then: "we should get a response without an error message"
        assert !resp.hasFault()
        assert  "myAddedMember@test.de" == resp.deleteMemberResponse.DeletedMember.email.toString()
    }

    def 'delete member with error because member does not exist'() {
        given: "no member with the same name"
        soapClient.send(deleteMemberBody("alreadyDeletedMember@test.de"))

        when: "we attempt to delete the member"
        def resp = soapClient.send(deleteMemberBody("alreadyDeletedMember@test.de"))

        then: "we should get a response with an error message explaining the reason"
        assert !resp.hasFault()
        assert  'Member with email="alreadyDeletedMember@test.de" does not exist.' == resp.deleteMemberResponse.DeletedMember.errorMessage.toString()
    }


    public static def String addMemberBody(String email, String password) {
        return """
                <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:mem="http://www.alexkrieg.de/cd-docker/persontracker">
                  <soapenv:Header/>
                    <soapenv:Body>
                      <mem:addMember>
                        <Member>
                          <email>${email}</email>
                          <password>${password}</password>
                        </Member>
                      </mem:addMember>
                    </soapenv:Body>
                </soapenv:Envelope>"""
    }

    public static def String deleteMemberBody(String email) {
        return """
      <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:mem="http://www.alexkrieg.de/cd-docker/persontracker">
        <soapenv:Header/>
        <soapenv:Body>
          <mem:deleteMember> 
            <Member>
              <email>${email}</email>
           </Member>
          </mem:deleteMember>
        </soapenv:Body>
     </soapenv:Envelope>"""
    }
}
