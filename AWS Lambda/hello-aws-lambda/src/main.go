package main

import (
	"log"

	"github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"

	"net/http"
)

func main() {
	lambda.Start(handler)
}

func handler(request events.APIGatewayProxyRequest) (events.APIGatewayProxyResponse, error) {
	log.Println("Hello, AWS Lambda!")

	response := events.APIGatewayProxyResponse{
		StatusCode: http.StatusOK,
	}

	return response, nil
}
