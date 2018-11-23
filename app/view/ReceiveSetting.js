import React, {Component} from 'react';
import {
    Platform, StyleSheet, Text, View, NativeModules, TouchableNativeFeedback,
    Alert
} from 'react-native';
import {
    Actions
} from 'react-native-router-flux';
import {Header, FormLabel, FormInput} from 'react-native-elements';
import MyStore from '../stores/MyStore';
import MyActions from '../actions/MyActions';

export default class ReceiveSetting extends Component {
    constructor(props) {
        super(props);
        let storeState = MyStore.getState();
        this.state = ({
            emailList: storeState.emailList
        });
    }

    componentDidMount() {
        MyStore.listen(this.onChange);
    }

    componentWillUnmount() {
        MyStore.unlisten(this.onChange);
    }

    onChange = (state) => {
        this.setState({
            emailList: state.emailList
        });
    }

    saveSetting = () => {
        MyActions.setReceiveEmail(this.state.emailList);
        Actions.pop();
    }
    changeEmail1 = (text) => {
        let emailList = this.state.emailList;
        emailList[0] = text;
        this.setState({
            emailList: emailList
        });
    }
    changeEmail2 = (text) => {
        let emailList = this.state.emailList;
        emailList[1] = text;
        this.setState({
            emailList: emailList
        });
    }

    render() {


        return (
            <View>
                <Header
                    leftComponent={{icon: 'close', color: '#fff', onPress: Actions.pop}}
                    centerComponent={{text: '设置接收邮箱', style: {color: '#fff'}}}
                    rightComponent={{icon: 'save', color: '#fff', onPress: this.saveSetting}}
                />

                <FormLabel>接收邮箱地址1</FormLabel>
                <FormInput onChangeText={this.changeEmail1}
                           value={this.state.emailList.length > 0 ? this.state.emailList[0] : ''}/>

                <FormLabel>接收邮箱地址2</FormLabel>
                <FormInput onChangeText={this.changeEmail2}
                           value={this.state.emailList.length > 1 ? this.state.emailList[1] : ''}/>
            </View>
        );
    }
}