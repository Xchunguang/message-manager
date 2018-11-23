/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View,NativeModules,TouchableHighlight,
        AppRegistry,
        Image,
        TouchableOpacity,
        ViewPagerAndroid,
        ScrollView,
        Navigator,
        ListView,
        Dimensions,
        WebView,
        ToastAndroid,
        DrawerLayoutAndroid,
        } from 'react-native';
import {
  Actions,
  Router,
  Scene,
  // Reducer,
} from 'react-native-router-flux';
import Locations from './app/view/Locations';
import MainView from './app/view/MainView';
import SettingView from './app/view/SettingView';
import InfoView from './app/view/InfoView';
import ReceiveSetting from './app/view/ReceiveSetting';
import ReadMeView from './app/view/ReadMeView';


const scenes = Actions.create(
  <View key="rootView">
  <Scene key="root"  hideNavBar={true}>
    <Scene key="main" title="主页" component={MainView} initial={true} />
    <Scene key="setting" direction="transverse" title="设置发送邮箱" component={SettingView} />
    <Scene key="infoDetail" direction="transverse" title="设置短信指令" component={InfoView}  />
    <Scene key="receive" direction="transverse" title="设置接受邮箱" component={ReceiveSetting}  />
    <Scene key="readme" direction="transverse" title="使用说明" component={ReadMeView}  />

  </Scene>
  </View>
);

type Props = {};
export default class App extends Component<Props> {
 

  render() {
    
    return (
          <Router scenes={scenes} />
    );
  }
}
