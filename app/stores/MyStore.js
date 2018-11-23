import alt from '../alt';
import MyActions from '../actions/MyActions';
import store from 'react-native-simple-store';
import {NativeModules,ToastAndroid } from 'react-native';
// @ see http://alt.js.org
const NativeIconAPI = NativeModules.MailModule;
 function ensureNativeModuleAvailable() {
    if (!NativeIconAPI) {
      if (Platform.OS === 'android') {
        throw new Error(
          'MailModule not available.'
        );
      }
      throw new Error(
        'MailModule not available.'
      );
    }
  }
class MyStore {

	constructor(){
		this.locations = [];

		this.state= {
			emailList:[],
			orderList:[{
				code:'getAll',
				behavior:'getAll'
			},{
				code:'getUnRead',
				behavior:'getUnRead'
			}],
			sendEmail:'',
			sendEmailPassword:''
		};

		 store.get('storeState').then((storeState) => {
	        if (storeState) {
	          this.setState({
	          	...storeState
	          });
	        }else{
	        	//初始化store
	        	store.save("storeState",this.state);
	        }
	      });

		this.bindListeners({
			handleUpdateLocations:MyActions.UPDATE_LOCATIONS,
			handleFetchLocations: MyActions.FETCH_LOCATIONS,
			handleSetSendEmail:MyActions.SET_SEND_EMAIL_INFO,
			setReceiveEmail:MyActions.SET_RECEIVE_EMAIL,
			setInfoOrder:MyActions.SET_INFO_ORDER
		});
	}

	handleUpdateLocations (locations){
		this.locations = locations;
	}

	handleFetchLocations() {
	    // reset the array while we're fetching new locations so React can
	    // be smart and render a spinner for us since the data is empty.
	    this.locations = [];
	}
	handleSetSendEmail(data){
		this.setState({
			sendEmail:data.sendEmail,
			sendEmailPassword:data.sendEmailPassword
		})
		store.update('storeState', {
			sendEmail: data.sendEmail,
			sendEmailPassword:data.sendEmailPassword
		})
		let infoStr = data.sendEmail+','+data.sendEmailPassword;
		NativeIconAPI.insertSendInfo(infoStr,(result)=>{
	      ToastAndroid.show("保存成功", ToastAndroid.SHORT);
	    });
	}

	setReceiveEmail=(arrData)=>{
		this.setState({
			emailList:arrData
		});
		store.update('storeState', {emailList:arrData});
		NativeIconAPI.insertEmailValue(arrData.join(','),(result)=>{
	      ToastAndroid.show("保存成功", ToastAndroid.SHORT);
	    });
	}

	setInfoOrder=(orderArr)=>{
		this.setState({
			orderList:orderArr
		});
		store.update('storeState',{orderList:orderArr});
	}
}
module.exports = alt.createStore(MyStore,'MyStore');