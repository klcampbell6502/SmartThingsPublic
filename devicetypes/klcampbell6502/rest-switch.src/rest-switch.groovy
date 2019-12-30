/**
 *  REST Switch
 *
 *  Copyright 2019 Kevin Campbell
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 */
metadata {
	definition (name: "REST Switch", namespace: "klcampbell6502", author: "Kevin Campbell", cstHandler: true) {
		capability "Switch"
	}


	simulator {
		// TODO: define status and reply messages here
	}

	// standard tile with actions
	standardTile("actionRings", "device.switch", width: 2, height: 2, canChangeIcon: true) {
		state "off", label: '${currentValue}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
		state "on", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00A0DC"
	}

	preferences {
        input "ip", "text", title: "IP Address", description: "IP Address in form 192.168.1.226", required: true, displayDuringSetup: true
		input "port", "text", title: "Port", description: "port in form of 8090", required: true, displayDuringSetup: true
    }
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'switch' attribute

}

// handle commands
def on() {
	log.debug "Executing 'on'"
    sendData("on")
    sendEvent(name: "switch", value: "on")
	// TODO: handle 'on' command
}

def off() {
	log.debug "Executing 'off'"
    sendData("off")
    sendEvent(name: "switch", value: "off")
	// TODO: handle 'off' command
}

private getHostAddress() {
    def ip = settings.ip
    def port = settings.port
    
    log.debug "Using ip: ${ip} and port: ${port} for device: ${device.id}"
    return ip + ":" + port
}

def sendData(String data){
	sendEthernet(data)
}

def sendEthernet(message){
	log.debug "Executing 'sendEthernet' ${message}"
	if (settings.ip != null && settings.port != null) {
        sendHubCommand(new physicalgraph.device.HubAction(
            method: "GET",
            path: "/screen/" + message,
            body: message,
            headers: [ HOST: "${getHostAddress()}" ]
        ))
    }
    else {
        state.alertMessage = "REST Device has not yet been fully configured. Click the 'Gear' icon, enter data for all fields, and click 'Done'"
        runIn(2, "sendAlert")   
    }
}

def installed() {
 	log.debug "Executing 'intalled'"
}