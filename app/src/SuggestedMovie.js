import {Card, CardBody, CardSubtitle, CardText, CardTitle} from "reactstrap";

const SuggestedMovie = ({movie}) => {
    const releaseYear = (date) => {
        return new Date(date).getFullYear();
    }

    return (
        <Card style={{minWidth: '16rem', maxWidth: '32rem', margin: '0.5rem'}}>
            <img alt="Sample" src="https://picsum.photos/300/200"/>
            <CardBody>
                <CardTitle tag="h5">
                    {movie.title}
                </CardTitle>
                <CardSubtitle className="mb-2 text-muted" tag="h6">
                    {releaseYear(movie.release)}
                </CardSubtitle>
                <CardText>
                </CardText>
            </CardBody>
        </Card>
    );
}

export default SuggestedMovie;
