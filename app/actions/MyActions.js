import alt from '../alt';

var mockData = [
  { id: 0, name: 'Abu Dhabi' },
  { id: 1, name: 'Berlin' },
  { id: 2, name: 'Bogota' },
  { id: 3, name: 'Buenos Aires' },
  { id: 4, name: 'Cairo' },
  { id: 5, name: 'Chicago' },
  { id: 6, name: 'Lima' },
  { id: 7, name: 'London' },
  { id: 8, name: 'Miami' },
  { id: 9, name: 'Moscow' },
  { id: 10, name: 'Mumbai' },
  { id: 11, name: 'Paris' },
  { id: 12, name: 'San Francisco' }
];

//模拟异步请求
var LocationSource = {
  fetch: function () {
    // returning a Promise because that is what fetch does.
    return new Promise(function (resolve, reject) {
      // simulate an asynchronous action where data is fetched on
      // a remote server somewhere.
      setTimeout(function () {
        // resolve with some mock data
        resolve(mockData);
      }, 250);
    });
  }
};

class MyActions {
	
	updateLocations(locations){
		return locations;
	}
	fetchLocations() {
	  return (dispatch) => {
	    // we dispatch an event here so we can have "loading" state.
	    // dispatch();
	    LocationSource.fetch()
	      .then((locations) => {
	        // we can access other actions within our action through `this.actions`
	        this.updateLocations(locations);
	      })
	      .catch((errorMessage) => {
	        console.log(errorMessage);
	      });
	    }
	}

	setSendEmailInfo(data){
		return data;
	}

	setReceiveEmail(arrData){
		return arrData;
	}

	setInfoOrder(orderArr){
		return orderArr;
	}
}
module.exports = alt.createActions(MyActions);