from http import HTTPStatus

import numpy as np
from flask import Flask, jsonify, request
from waitress import serve

from utils import model, сanvas_processing, NAMES_CLASSES, config

app = Flask(__name__)


@app.route('/predict', methods=['POST'])
def apicall():
    data = request.get_json()

    try:
        predictions = model.predict(сanvas_processing(data))

        predictions_of_current_shape = np.array(predictions)[0]

        predictions_indexes_without_sorting = np.argpartition(predictions_of_current_shape,
                                                              -config.getint('Main', 'Top_k'))[
                                              -config.getint('Main', 'Top_k'):]

        predictions_indexes = (predictions_indexes_without_sorting[
            np.argsort(predictions_of_current_shape[predictions_indexes_without_sorting])])

        list_top_classes = [NAMES_CLASSES[predictions_indexes[config.getint('Main', 'Top_k') - 1 - i]] for i in
                            range(config.getint('Main', 'Top_k'))]

    except Exception as e:
        return bad_data_request(e)

    responses = jsonify(list_top_classes)
    responses.status_code = HTTPStatus.OK

    return responses


@app.errorhandler(Exception)
def bad_data_request(error):
    message_error = [str(x) for x in error.args]
    message = {
        'status': HTTPStatus.BAD_REQUEST,
        'message': 'Bad Request: ' + request.url + '--> Please check your data payload...',
        'error': {
            'type': error.__class__.__name__,
            'message': message_error,
        }
    }
    resp = jsonify(message)
    resp.status_code = HTTPStatus.BAD_REQUEST

    return resp


@app.errorhandler(HTTPStatus.BAD_REQUEST)
def bad_request(error=None):
    message = {
        'status': HTTPStatus.BAD_REQUEST,
        'message': 'Bad Request: ' + request.url + '--> Please check your data payload...',
    }
    resp = jsonify(message)
    resp.status_code = HTTPStatus.BAD_REQUEST

    return resp


if __name__ == '__main__':
    serve(app, host=config['Flask']['Host'], port=config['Flask']['Port'])
