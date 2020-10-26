import firebase from 'firebase';

const config = {
  apiKey: "AIzaSyDZarcPHhP-2RQuSgxD9sDFfPeCaLIWBuI",
  authDomain: "fin-ai-f4636.firebaseapp.com",
  databaseURL: "https://fin-ai-f4636.firebaseio.com",
  projectId: "fin-ai-f4636",
  storageBucket: "fin-ai-f4636.appspot.com",
  messagingSenderId: "499022803126",
  appId: "1:499022803126:web:d00bb05cfb4b59d665b617",
  measurementId: "G-6R8Y3GWHLT"
};

const fire = firebase.initializeApp(config);
export default fire;