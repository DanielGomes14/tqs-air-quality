import Typography from "@material-ui/core/Typography";
import { makeStyles } from '@material-ui/core/styles';
import React from 'react';
import {Card,CardContent, CardHeader,} from "@material-ui/core"
import {Row,Col} from "react-bootstrap"
const useStyles = makeStyles((theme) => ({
    root: {
        
        height: '100%'
        
    },
    media: {
        height: 0,
        paddingTop: '56.25%', // 16:9
    },
    expand: {
        transform: 'rotate(0deg)',
        marginLeft: 'auto',
        transition: theme.transitions.create('transform', {
        duration: theme.transitions.duration.shortest,
        }),
    },
    expandOpen: {
        transform: 'rotate(180deg)',
    },
}));
function QualityResults(props){
    const city_name =  props.data.city_name
    const classes = useStyles();
    const [expanded, setExpanded] = React.useState(false);

    const handleExpandClick = () => {
        setExpanded(!expanded);
    };

    return (
        <>
        <Typography
            className={"MuiTypography--heading"}
            variant={"h4"}
            color= "primary"
            align = 'center'
        >
        {city_name},{props.data.country_code}
        </Typography>
        <Typography
            variant={"overline"}
            color= "primary"
            gutterBottom
            display="block"
            align = 'center'
        >
        Location: {props.data.lat},{props.data.lon}
        </Typography>
        <Row >
        <Col md="4">
        <Card className={classes.root}>
        <CardHeader
        title="Ozone (o3)"
        align='center'
        />
        <CardContent>
        <Typography variant="h4" align = 'center' color="primary">
            {props.data.data[0].o3}
        </Typography>
        
        
        </CardContent>
        </Card>
        </Col>
        <Col md="4">
        <Card className={classes.root}>
            <CardHeader
            title="Carbon Monoxide (co)"
            align='center'
            />
            <CardContent>
                <Typography variant="h4" align = 'center' color="primary">
                    {props.data.data[0].co}
                </Typography>
            </CardContent>
            </Card>
        </Col>   
        <Col md="4">
            <Card className={classes.root}>
                <CardHeader
                title="Sulfur Dioxide (co)"
                align='center'
                />
                <CardContent>
                <Typography variant="h4" align = 'center' color="primary">
                    {props.data.data[0].so2}
                </Typography>
                </CardContent>
            </Card>
        </Col>
        </Row>
        <br/>
        <br/>
        <Row >
            <Col md="4">
                <Card className={classes.root}>
                    <CardHeader
                    title="Nitrogren Dioxide (no2)"
                    align='center'
                    />
                    <CardContent>
                    <Typography variant="h4" align = 'center' color="primary">
                        {props.data.data[0].no2}
                    </Typography>
                    </CardContent>
                </Card>
            </Col>
            <Col md="4">
                <Card className={classes.root}>
                    <CardHeader
                    title="Inh. Particl. Manner (pm10)"
                    align='center'
                    />
                    <CardContent>
                    <Typography variant="h4" align = 'center' color="primary">
                        {props.data.data[0].pm10}
                    </Typography>
                    </CardContent>
                </Card>
            </Col>
            <Col md="4">
                <Card className={classes.root}>
                    <CardHeader
                    title="Fine Particl. Manner (pm25)"
                    align='center'
                    />
                    <CardContent>
                    <Typography variant="h4" align = 'center' color="primary">
                        {props.data.data[0].pm25}
                    </Typography>
                    </CardContent>
                </Card>
            </Col>
        </Row>
        </>
    )
}
export default  QualityResults;