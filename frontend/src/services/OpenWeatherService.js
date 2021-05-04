import {API_BASE} from "config/urls"

class OpenWeatherService{

    getForecastByLatAndLon(coords) {
        let [lat, lon] = [null,null]
        let url = 'forecast';
        if(typeof coords == 'string'){
            if(coords.includes(',')){
                let search_arr = coords.split(',');
                lat = search_arr[0];
                lon = search_arr[1];
            }
        }
        else{
            lat = coords.lat
            lon = coords.lon
        }
        console.log("lat" + lat + "lon" + lon)
        if(lat != null && lon != null){
            url+='?lat=' + lat
            url+='&lon=' + lon
        } 
        console.log(url)
        return fetch(API_BASE + url, {
            method: 'GET',
            mode: 'cors',
        })
    }
}
export default new OpenWeatherService();