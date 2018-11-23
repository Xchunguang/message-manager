import React, {Component} from 'react';
import {
    Platform, StyleSheet, Text, View, NativeModules, TouchableNativeFeedback,
    Image,
    Alert
} from 'react-native';
import {
    Actions
} from 'react-native-router-flux';
import {Header, FormLabel, FormInput, Card, ListItem} from 'react-native-elements';

const users = [
    {
        name: 'brynn',
        avatar: 'https://s3.amazonaws.com/uifaces/faces/twitter/brynn/128.jpg'
    }, {
        name: 'brynn1',
        avatar: 'https://s3.amazonaws.com/uifaces/faces/twitter/brynn/128.jpg'
    },
]
export default class ReadMeView extends Component {
    saveSetting = () => {
        Actions.pop();
    }

    render() {


        return (
            <View>
                <Header
                    leftComponent={{icon: 'close', color: '#fff', onPress: Actions.pop}}
                    centerComponent={{text: '使用说明', style: {color: '#fff'}}}
                />

                <Card title="CARD WITH DIVIDER">
                    {
                        users.map((u, i) => {
                            return (
                                <View key={i}>
                                    <Image
                                        resizeMode="cover"
                                        source={{uri: u.avatar}}
                                    />
                                    <Text>{u.name}</Text>
                                </View>
                            );
                        })
                    }
                </Card>

                <Card containerStyle={{padding: 0}}>
                    {
                        users.map((u, i) => {
                            return (
                                <ListItem
                                    key={i}
                                    roundAvatar
                                    title={u.name}
                                    avatar={{uri: u.avatar}}
                                />
                            );
                        })
                    }
                </Card>

            </View>
        );
    }
}