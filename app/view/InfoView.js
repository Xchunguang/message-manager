import React, {Component} from 'react';
import {
    Platform, StyleSheet, Text, View, NativeModules, TouchableHighlight,

} from 'react-native';
import {Header, FormLabel, FormInput} from 'react-native-elements';
import {
    Actions
} from 'react-native-router-flux';
import MyStore from '../stores/MyStore';
import MyActions from '../actions/MyActions';

export default class InfoView extends Component {
    constructor(props) {
        super(props);
        let storeState = MyStore.getState();
        this.state = ({
            orderList: storeState.orderList
        });
    }

    saveInfo = () => {
        MyActions.setInfoOrder(this.state.orderList);
        Actions.pop();
    }
    getAllCode = (text) => {
        let orderList = this.state.orderList;
        orderList[0].behavior = text;
        this.setState({
            orderList: orderList
        })
    }
    getUnReadCode = (text) => {
        let orderList = this.state.orderList;
        orderList[1].behavior = text;
        this.setState({
            orderList: orderList
        })
    }

    render() {
        return (
            <View>
                <Header
                    leftComponent={{icon: 'close', color: '#fff', onPress: Actions.pop}}
                    centerComponent={{text: '设置短信指令', style: {color: '#fff'}}}
                    rightComponent={{icon: 'save', color: '#fff', onPress: this.saveInfo}}
                />
                <FormLabel>获取所有短信</FormLabel>
                <FormInput onChangeText={this.getAllCode} value={this.state.orderList[0].behavior}/>

                <FormLabel>获取未读短信</FormLabel>
                <FormInput onChangeText={this.getUnReadCode} value={this.state.orderList[1].behavior}/>
            </View>
        );
    }
}