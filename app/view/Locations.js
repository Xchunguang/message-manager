import React, {Component} from 'react';
import MyStore from '../stores/MyStore';
import MyActions from '../actions/MyActions';
import { FlatList, StyleSheet, Text, View } from 'react-native';

/**
 * alt 示例，整合state
 */
export default class Locations extends Component{
	constructor(props){
		super(props);
		this.state =({
			locations:[]
		});
		
	}

	getInitialState(){
		return MyStore.getState();
	}

	componentDidMount(){
		MyStore.listen(this.onChange);

		MyActions.fetchLocations();
	}

	componentWillUnmount(){
		MyStore.unlisten(this.onChange);
	}

	onChange =(state) =>{
		this.setState({
			locations:state.locations
		});
	}
	render(){
		return (
			<View>
				<FlatList
		          data={this.state.locations}
		          renderItem={({item}) => <Text key={item.id}>{item.name}</Text>}
		        />
			</View>
		);
	}
}