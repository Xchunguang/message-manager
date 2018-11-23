import React, {Component} from 'react';
import {
    Platform, StyleSheet, Text, View, NativeModules, TouchableHighlight,
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
// import { Toolbar } from 'react-native-material-ui';
import {Header, SocialIcon, List, ListItem, Avatar, Icon} from 'react-native-elements';
import SettingView from './SettingView';
import InfoView from './InfoView';
import ReceiveSetting from './ReceiveSetting';
import ReadMeView from './ReadMeView';


const NativeIconAPI = NativeModules.MailModule;
const {width, height} = Dimensions.get('window');

const instructions = Platform.select({
    ios: 'Press Cmd+R to reload,\n' + 'Cmd+D or shake for dev menu',
    android:
    'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});

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

export default class MainView extends Component {

    constructor(props) {
        super(props);
        this.state = ({
            total: ''
        })
    }

    componentDidMount() {
        this.onRefresh();
    }

    _onPressButton = () => {

        ensureNativeModuleAvailable();
        NativeIconAPI.sendMail("I am react", (result) => {
            console.log(result);
        });
    }
    onGetMessage = () => {
        ensureNativeModuleAvailable();
        NativeIconAPI.getMessage((result) => {
            console.log(result);
        });
    }

    insertEmail = () => {
        let emailList = '13194511605@163.com,1325669800@qq.com';
        NativeIconAPI.insertEmailValue(emailList, (result) => {
            ToastAndroid.show(result, ToastAndroid.SHORT);
        });
    }


    readEmail = () => {
        NativeIconAPI.getEmailValue((result) => {
            ToastAndroid.show(result, ToastAndroid.SHORT);
        });
    }

    handleDrawerOpen = () => {
        //使用ToastAndroid组件弹出一个原生的Toast
        // ToastAndroid.show("open drawer", ToastAndroid.SHORT);
    }

    handleDrawerClose = () => {
        // ToastAndroid.show("close drawer", ToastAndroid.SHORT);
    }

    open = () => {
        this.drawer.openDrawer();
    }

    close = () => {
        this.drawer.closeDrawer();
    }

    openSendModal = () => {
        this.drawer.closeDrawer();
        Actions.setting();
    }


    openReceiveModal = () => {
        this.drawer.closeDrawer();
        Actions.receive();
    }

    openInfoModal = () => {
        this.drawer.closeDrawer();
        Actions.infoDetail();
    }
    openReadMe = () => {
        this.drawer.closeDrawer();
        Actions.readme();
    }
    onRefresh = () => {
        NativeIconAPI.getTotalReceiveNum((result) => {
            this.setState({
                total: result
            })
        });
    }

    render() {

        var settingView = (
            <View>
                <SettingView draw={this.setDrawer}/>
            </View>
        );

        var navigationView = (
            <View style={{flex: 1, backgroundColor: '#ccc'}}>
                <View style={{alignItems: 'center', marginTop: 10}}>
                    <Avatar
                        large
                        rounded
                        title="MM"
                        onPress={() => console.log("Works!")}
                        activeOpacity={0.7}

                    />
                </View>
                <List>
                    <ListItem
                        key={1}
                        title='设置发送邮箱'
                        leftIcon={{name: 'send'}}
                        onPress={this.openSendModal}
                    />
                    <ListItem
                        key={2}
                        title='设置接收邮箱'
                        leftIcon={{name: 'message'}}
                        onPress={this.openReceiveModal}
                    />
                    <ListItem
                        key={3}
                        title='设置短信指令'
                        leftIcon={{name: 'memory'}}
                        onPress={this.openInfoModal}
                    />
                    <ListItem
                        key={4}
                        title='使用说明'
                        leftIcon={{name: 'info'}}
                        onPress={this.openReadMe}
                    />
                </List>
                <SocialIcon
                    title='Github'
                    button
                    type='github'
                />
            </View>
        );
        return (

            <DrawerLayoutAndroid
                ref={(drawer) => {
                    this.drawer = drawer;
                }}
                drawerWidth={width * 0.7}
                onDrawerClose={this.handleDrawerClose}
                onDrawerOpen={this.handleDrawerOpen}
                drawerPosition={DrawerLayoutAndroid.positions.Left}
                renderNavigationView={() => navigationView}>


                    <Header
                        leftComponent={{icon: 'menu', color: '#fff', onPress: this.open}}
                        centerComponent={{text: '短信助手', style: {color: '#fff'}}}
                        rightComponent={{icon: 'refresh', color: '#fff', onPress: this.onRefresh}}
                    />
                    <Text style={{fontSize: 20, textAlign: 'left', marginTop: 20, marginLeft: 20}}>发送短信数：</Text>
                    <View style={{flexDirection: 'row', justifyContent: 'center', marginTop: 40}}>
                        <Text style={{fontSize: 50, color: 'black', textAlign: 'center'}}>{this.state.total}</Text>
                        <Text style={{fontSize: 25, marginTop: 25}}>条</Text>
                    </View>

            </DrawerLayoutAndroid>
        )
    }
}
const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'flex-start',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
    },
    welcome: {
        fontSize: 20,
        textAlign: 'center',
        margin: 10,
    },
    instructions: {
        textAlign: 'center',
        color: '#333333',
        marginBottom: 5,
    },
    drawerStyle: {
        backgroundColor: '#6699FF'
    },
    textContainer: {
        flex: 1,
        alignItems: 'center',
        flexDirection: 'column',
        justifyContent: 'center',
    },
    textStyle: {
        margin: 10,
        color: 'black',
        textAlign: 'center'
    },
    textSmall: {
        fontSize: 15,
    },
    textLarge: {
        fontSize: 35,
    }
});
