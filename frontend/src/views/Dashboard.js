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
      has_error: false,
      error_message: ""
    }
  }
  handler = (city_name) => {
    WeatherBitService.searchbyCity(city_name)
    .then( (res) => {
      if(res.status == 200){
        this.setState({has_error:false, error_message:""})
        return res.json();
      }
      this.setState({has_error:true, error_message: "City And/Or Country Not Found!"})
    }).then((res) => {
        this.setState({air_quality_data:res})
    })
    .catch(() => {
      this.setState({has_error:true, error_message:"Some Error Occured"})

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
                <SearchBar label= "Current-Time Air Quality Data Search" placeholder="Aveiro,PT" handler = {this.handler}/>
                { this.state.air_quality_data != null ?
                <QualityResults  data = {this.state.air_quality_data}/>
                : (this.state.has_error)?
                <Typography variant="body1" align = 'center' color="error">
                {this.state.error_message}
                </Typography>
                : ""
              }
              </Card.Body>
              <Card.Footer>
                <hr></hr>
                <div className="stats">
                  <i className="fas fa-history"></i>
«                </div>
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
