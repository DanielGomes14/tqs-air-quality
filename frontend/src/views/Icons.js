import React from "react";

// react-bootstrap components
import {
  Badge,
  Button,
  Card,
  Navbar,
  Nav,
  Container,
  Row,
  Col,
} from "react-bootstrap";

class Icons extends React.Component{
  constructor(props){
    super(props)
  }
  render() {
    return(
      <>
      <Container fluid>
        <Row>
          <Col md="12">
            <Card>
              <Card.Header>
                <Card.Title as="h4">100 Awesome Nucleo Icons</Card.Title>
                <p className="card-category">
                  Handcrafted by our friends from{" "}
                  <a href="https://nucleoapp.com/?ref=1712">NucleoApp</a>
                </p>
              </Card.Header>
              <Card.Body className="all-icons">
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </>
    )
  }
}


export default Icons;
