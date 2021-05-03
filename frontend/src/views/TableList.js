import React from "react";
import CitiesService from "../services/CitiesService"
import CitiesTable from "../components/CitiesTable/CitiesTable"
// react-bootstrap components
import {
  Card,
  Container,
  Row,
  Col,
} from "react-bootstrap";

class TableList extends React.Component {
  constructor(props){
    super(props)
    this.state = {
      cities_list_data: null
    }
  }
  componentDidMount() {
    CitiesService.getAllCities()
    .then( (res) => {
      if(res.status == 200)
        return res.json()
    })
    .then( ( res)=> {
      console.log(res)
      this.setState({cities_list_data: res})
    })
  }
  render(){
    return (
      <>
      <Container fluid>
        <Row>
          <Col md="12">
            <Card className="strpied-tabled-with-hover">
              <Card.Header>
                <Card.Title as="h4">Cities Details and Air Quality Forecast</Card.Title>
                <p className="card-category">
                  Check a list of some Cities and click on the button to check the forecast 
                  of Air Quality.
                </p>
              </Card.Header>
              <Card.Body className="table-full-width table-responsive px-0">
                {this.state.cities_list_data != null ? 
                <CitiesTable cities_list_data = {this.state.cities_list_data} />
                :
                null
              }
                
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </>

    )
  }
}

export default TableList;
