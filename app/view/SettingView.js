import React, {Component} from 'react';
import {
    Platform, StyleSheet, Text, View, NativeModules, TouchableHighlight, ToastAndroid,
    Image, TextInput
} from 'react-native';
import {
    Actions
} from 'react-native-router-flux';
import {Header, FormLabel, FormInput, FormValidationMessage} from 'react-native-elements';
import MyStore from '../stores/MyStore';
import MyActions from '../actions/MyActions';

export default class SettingView extends Component {
    constructor(props) {
        super(props);
        let storeState = MyStore.getState();
        this.state = ({
            sendEmail: storeState.sendEmail,
            sendEmailPassword: storeState.sendEmailPassword
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
            sendEmail: state.sendEmail,
            sendEmailPassword: state.sendEmailPassword
        });
    }

    saveSetting = () => {
        let data = {
            sendEmail: this.state.sendEmail,
            sendEmailPassword: this.state.sendEmailPassword
        };
        MyActions.setSendEmailInfo(data);
        Actions.pop();
    }
    changeName = (text) => {
        this.setState({
            sendEmail: text
        })
    }
    changePassword = (text) => {
        this.setState({
            sendEmailPassword: text
        })
    }

    render() {

        return (
            <View>
                <Header
                    leftComponent={{icon: 'close', color: '#fff', onPress: Actions.pop}}
                    centerComponent={{text: '设置发送邮箱', style: {color: '#fff'}}}
                    rightComponent={{icon: 'save', color: '#fff', onPress: this.saveSetting}}
                />

                <FormLabel>邮箱地址</FormLabel>
                <FormInput onChangeText={this.changeName} value={this.state.sendEmail}/>

                <FormLabel>邮箱密码</FormLabel>
                <FormInput onChangeText={this.changePassword} password={true} value={this.state.sendEmailPassword}/>


            </View>
        );
    }
}