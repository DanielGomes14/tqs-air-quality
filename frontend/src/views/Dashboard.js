import React from "react";
// react-bootstrap components
import Typography from '@material-ui/core/Typography';
import {
  Card,
  Container,
  Row,
  Col,
} from "react-bootstrap";

import QualityResults from "../components/AirQualityResults/AirQualityResults"
import SearchBar from "../components/SearchBar/SearchBar"
import WeatherBitService from "../services/WeatherBitService"

class Dashboard extends React.Component {
  

  constructor (props){
    super(props);
    this.state = {
      air_quality_data : null,
    }
  }
  handler = (city_name) => {
    console.log(city_name)
    WeatherBitService.searchbyCity(city_name)
    .then( (res) => {
      if(res.status == 200){
        return res.json();
      }
    }).then((res) => {
        console.log(res)
        this.setState({air_quality_data:res})
        console.log(this.state.air_quality_data)
    })
    
  }
  render(){
    return(
      <>
      <Container fluid>
        <Row>
        </Row>
        <Row>
          <Col md="12">
            <Card>
              <Card.Header>
                <Card.Title as="h4">Air Quality Data By City</Card.Title>
                <p className="card-category">Accurate Data of Air Quality</p>
              </Card.Header>
              <Card.Body>
                <SearchBar label= "City Search" placeholder="Aveiro,PT" handler = {this.handler}/>
                { this.state.air_quality_data != null ?
                <QualityResults  data = {this.state.air_quality_data}/>
                : ""
              }
              </Card.Body>
              <Card.Footer>
                <hr></hr>
                <div className="stats">
                  <i className="fas fa-history"></i>
                  Updated 3 minutes ago
                </div>
              </Card.Footer>
            </Card>
          </Col>
        </Row>
      </Container>
    </>
    )
  }
}


export default Dashboard;
