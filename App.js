/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React from 'react';
import {
  Linking,
  PermissionsAndroid,
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  useColorScheme,
  View,
} from 'react-native';

import {
  Colors,
  DebugInstructions,
  Header,
  LearnMoreLinks,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';

import PhoneScreen from './PhoneScreen';
import { DeviceEventEmitter } from 'react-native';
import firebase from '@react-native-firebase/app';


// Your secondary Firebase project credentials...


const initializaFireBase = async () => {
  const credentials = {
    clientId: '1088794682667-4kl3rdc3iidv1gsnqgvuj6nreq3d2dn3.apps.googleusercontent.com',
    appId: 'APcbbbbc534c9fe8b089e1f4d397aa253a',
    apiKey: 'adcc84d5327e43790c63988aa856ef06',
    databaseURL: '',
    storageBucket: 'sample-voice-project-7dc08.appspot.com',
    messagingSenderId: '',
    projectId: 'sample-voice-project-7dc08',
  };
  
  const config = {
    name: 'DEMO APP',
  };
  try {
  await firebase.app();
  console.log(firebase.app());
  } catch (e) {
    console.log(e);
  }
}
const requestCameraPermission = async () => {
  try {
    const granted = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.RECORD_AUDIO,
      {
        title: "Audio Permission Required",
        message:
          "This app needs access to your microphone",
        buttonNeutral: "Ask Me Later",
        buttonNegative: "Cancel",
        buttonPositive: "OK"
      }
    );
    if (granted === PermissionsAndroid.RESULTS.GRANTED) {
      console.log("You can use the audio");
    } else {
      console.log("Camera permission denied");
    }
  } catch (err) {
    console.warn(err);
  }
};

const App = () => {
  const isDarkMode = useColorScheme() === 'dark';

  React.useEffect(() =>{
    initializaFireBase();
    requestCameraPermission();
    // Twilio.initWithToken('eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImN0eSI6InR3aWxpby1mcGE7dj0xIn0.eyJqdGkiOiJTS2Y3YzliOWRiODU5NTM2OTRmNjEzYzBmOTc3YWQ0YmNjLTE2NDMwMzIyMDciLCJncmFudHMiOnsiaWRlbnRpdHkiOiJ1c2VyIiwidm9pY2UiOnsib3V0Z29pbmciOnsiYXBwbGljYXRpb25fc2lkIjoiQVBjYmJiYmM1MzRjOWZlOGIwODllMWY0ZDM5N2FhMjUzYSJ9LCJwdXNoX2NyZWRlbnRpYWxfc2lkIjoiQ1I4ZGNiMmFhY2E3Nzk4NmEyOGY4MWQ2ZjYzODZjNGZiMyJ9fSwiaWF0IjoxNjQzMDMyMjA3LCJleHAiOjE2NDMwMzU4MDcsImlzcyI6IlNLZjdjOWI5ZGI4NTk1MzY5NGY2MTNjMGY5NzdhZDRiY2MiLCJzdWIiOiJBQzAwNjUxZDg0YjIzMDczZjM5ZmE3NjdkZWVlODNlYmIyIn0.kpauyYwks1PDZPfzUbgPtyeBaKA-Z7oswnFx2opLiTI');
  // Twilio.addEventListener('deviceDidStartListening', this._deviceDidStartListening);
  // Twilio.addEventListener('deviceDidStopListening', this._deviceDidStopListening);
  // Twilio.addEventListener('deviceDidReceiveIncoming', ()=> alert('device ready'));
  // Twilio.addEventListener('connectionDidStartConnecting', this._connectionDidStartConnecting);
  // Twilio.addEventListener('connectionDidConnect', this._connectionDidConnect);
  // Twilio.addEventListener('connectionDidDisconnect', this._connectionDidDisconnect);
  // Twilio.addEventListener('deviceReady', ()=> alert('device okay'));
  // Twilio.connect({To: '+918309677743'});  
  // alert('hello');
  // async function fetchData() {
  //   fetch('https://quickstart-9775-dev.twil.io/place-call?to=+918309677743')
  // .then(response => response.json())
  // .then(data => console.log(data));
  // // await Linking.openURL('https://quickstart-9775-dev.twil.io/place-call?to=+918309677743');
  // alert('hello world');
  // }
  // fetchData();

  // Twilio.initWithToken('eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImN0eSI6InR3aWxpby1mcGE7dj0xIn0.eyJqdGkiOiJTS2Y3YzliOWRiODU5NTM2OTRmNjEzYzBmOTc3YWQ0YmNjLTE2NDMwMzIyMDciLCJncmFudHMiOnsiaWRlbnRpdHkiOiJ1c2VyIiwidm9pY2UiOnsib3V0Z29pbmciOnsiYXBwbGljYXRpb25fc2lkIjoiQVBjYmJiYmM1MzRjOWZlOGIwODllMWY0ZDM5N2FhMjUzYSJ9LCJwdXNoX2NyZWRlbnRpYWxfc2lkIjoiQ1I4ZGNiMmFhY2E3Nzk4NmEyOGY4MWQ2ZjYzODZjNGZiMyJ9fSwiaWF0IjoxNjQzMDMyMjA3LCJleHAiOjE2NDMwMzU4MDcsImlzcyI6IlNLZjdjOWI5ZGI4NTk1MzY5NGY2MTNjMGY5NzdhZDRiY2MiLCJzdWIiOiJBQzAwNjUxZDg0YjIzMDczZjM5ZmE3NjdkZWVlODNlYmIyIn0.kpauyYwks1PDZPfzUbgPtyeBaKA-Z7oswnFx2opLiTI');
  setTimeout(()=>{
    // Twilio.connect({To: '+918309677743', FromName : '+15878442715'});

  }, 10000)  


  })
  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  const endCall = () =>{
    const accountSid = "AC00651d84b23073f39fa767deee83ebb2";
    const authToken = "94bb30281bff0f28a8468c82ffab8737";

    // const client = require('twilio')(accountSid, authToken);
  
    // client.calls('CA4d609c7dfe0d0ea28afef23d5e131e5c')
    //       .update({status: 'completed'})
    //       .then(call => console.log(call.to));
    //   console.log('make call is triggered');
    // callback(null, twiml);
  }
  

  return (
    <SafeAreaView style={{flex:1}}>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
            <PhoneScreen endCall={endCall} /> 
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
  },
  highlight: {
    fontWeight: '700',
  },
});

const onSessionConnect = (event) => {
  console.log(event);  
};

DeviceEventEmitter.addListener('onSessionConnect', onSessionConnect);

export default App;
