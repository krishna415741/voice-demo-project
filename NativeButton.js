import React, { useState } from 'react';
import { NativeModules, Button, Text, View, TextInput, TouchableOpacity, NativeEventEmitter } from 'react-native';
const { TwilioModule } = NativeModules;
import { Stopwatch, Timer } from 'react-native-stopwatch-timer';

const NewModuleButton = ({endCall}) => {
  const [callStarted, setIsCallStarted] = useState(false);
  const [value, setValue] = useState(null);
  const [timerStart, setTimerStart] = useState(false);
  const [stopwatchStart, setStopWatchStart] = useState(false);
  const [stopwatchReset, setStopWatchReset] = useState(false);
  const onPress = async () => {
    var accountSid = "AC00651d84b23073f39fa767deee83ebb2";
    var keySid = "SKf7c9b9db85953694f613c0f977ad4bcc"; 
    var secret = "y6QKBAl4lxZggzOojQjIKRmfvR4mdBur"; 
    var applicationSid = "APcbbbbc534c9fe8b089e1f4d397aa253a";
    TwilioModule.generateAccessToken(accountSid, keySid, secret, applicationSid);
    await TwilioModule.createCallEvent(value,(c) => {
      // alert("hello world");
      setIsCallStarted(true);
      console.log(c)
      toggleStopwatch();

    });

    const eventEmitter = new NativeEventEmitter(TwilioModule);
    eventEmitter.addListener('CallEnded', (event) => {
      console.log(event.eventProperty);
      // alert("bye bye");
      resetStopWatch();
      setIsCallStarted(false); // "someValue"
   });
};

const rejectCalls = () => {
  setIsCallStarted(false);
  TwilioModule.rejectCalls();
  resetStopWatch();
};

const holdCalls = () => {
  TwilioModule.holdCalls();
};

const muteCalls = () => {
  TwilioModule.muteCalls();
};

const toggleTimer = () => {
  setTimerStart(!timerStart);
}

const toggleStopwatch = () => {
  setStopWatchStart(!stopwatchStart);
  setStopWatchReset(false);
}

const resetStopWatch = () => {
  setStopWatchStart(false);
  setStopWatchReset(true);
}

const getFormattedTime = (time) => {

}

  return (
      <View style={{flex:1, backgroundColor:'white', display:'flex'}}>
          <View style={{flex:3, padding:15}}>          
          <Text style={{padding:10}}>Enter Mobile Number</Text>
          <TextInput style={{borderWidth:1, borderRadius:15, fontSize:20}}  value={value} onChangeText={(e)=>{
            setValue(e)
          }} />
          {callStarted && <View style={{justifyContent:'center',padding:10,alignItems:'center'}}>
          <Stopwatch start={stopwatchStart}
          getTime={getFormattedTime}
          reset={stopwatchReset}
          />
          </View>}
          </View>
          <View>
          {!callStarted && <View style={{justifyContent:'center',alignItems:'center',padding:10,borderRadius:20}}>
          
    <TouchableOpacity style={{backgroundColor:'green',padding:20,borderRadius:190}} onPress={onPress}>
    <Text style={{color:'white', fontSize:20}}>  Call  </Text>  
    </TouchableOpacity>
    
    </View>}
    {callStarted && <View>
    <Button
      title="Reject"
      color="red"
      onPress={rejectCalls}
    />
    </View>}
    {callStarted && <View>
    <Button
      title="Hold"
      color="black"
      onPress={holdCalls}
    />
    </View>}
    {callStarted && <View >
    <Button
      title="Mute"
      color="#841584"
      onPress={muteCalls}
    />
    </View>}
    </View>
    </View>
  );
};

export default NewModuleButton;