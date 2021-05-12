import React from "react";
import CacheService from "../services/CacheService"
import CacheCards from "../components/CacheCards/CacheCards"
// react-bootstrap components
import {
  Card,
  Container,
  Row,
  Col,
} from "react-bootstrap";

class Cache extends React.Component{
  constructor(props){
    super(props)
    this.state = {
      cache_stats_data : null
    }
  }

  componentDidMount() {
    CacheService.searchCacheStats()
    .then( (res) => {
        if(res.status == 200)
          return res.json()
        
    })
    .then( (res) => {
      console.log(res)
      this.setState( {cache_stats_data: res})
    })
  }

  render() {
    return(
      <>
      <Container fluid>
        <Row>
          <Col md="12">
            <Card>
              <Card.Header>
                <Card.Title as="h4">Cache Statistics</Card.Title>
              </Card.Header>
              <Card.Body className="all-icons">
              {this.state.cache_stats_data != null ?
                <CacheCards cachedata = {this.state.cache_stats_data}/>
              : ""
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


export default Cache;
