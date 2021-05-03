import {API_BASE} from "config/urls"

class OpenWeatherService{

    getForecastByLatAndLon(lat,lon) {
        let url = 'forecast';
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