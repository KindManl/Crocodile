from configparser import ConfigParser

import numpy as np
from keras.saving.save import load_model
from skimage.transform import resize

config = ConfigParser()

config.read('config.ini')


def preprocess_image(data):
    data = resize(data, (config.getint('Main', 'ImageSizeByX'), config.getint('Main', 'ImageSizeByY')),
                  anti_aliasing=True)

    data = np.array(data, dtype=np.float32)

    img = np.reshape(data, (-1, config.getint('Main', 'ImageSizeByX'), config.getint('Main', 'ImageSizeByY'), 1))

    return img


def сanvas_processing(data):
    data = np.array(data)

    test = np.where(data == 0)

    x = min(test[0])

    y = min(test[1])

    x1 = max(test[0])

    y1 = max(test[1])

    data = 1 - data[x:x1, y:y1] / 255

    image = preprocess_image(data)

    return image


model = load_model(config['PATH']['Model'],
                   custom_objects={config['CustomObject']['Top_2_Acc']: None, config['CustomObject']['Top_3_Acc']: None,
                                   config['CustomObject']['Top_4_Acc']: None,
                                   config['CustomObject']['Top_5_Acc']: None})

with  open(config['PATH']['ClassNames'], "r", encoding='UTF-8') as file:
    NAMES_CLASSES = file.readlines()

    if NAMES_CLASSES.__len__() != config.getint('Main', 'NumberOfClasses'):
        raise Exception("Len NAMES_CLASSES != NumberOfClasses, {} != {}".format(NAMES_CLASSES.__len__(),
                                                                                config.getint('Main',
                                                                                              'NumberOfClasses')))
