import pytest
from runner import get_specs
from recognizers_choice import recognize_boolean

MODELFUNCTION = {'Boolean': recognize_boolean}


@pytest.mark.parametrize('culture, model, options, context, source, expected_results', get_specs(recognizer='Choice', entity='Model'))
def test_choice_recognizer(culture, model, options, context, source, expected_results):
    results = get_results(culture, model, source)
    assert len(results) == len(expected_results)
    print(type(results[0]))
    print(type(expected_results[0]))
    for actual, expected in zip(results, expected_results):
        print(type(actual))
        assert actual.type_name == expected['TypeName']
        assert actual.text == expected['Text']
        assert actual.start == expected['Start']
        assert actual.end == expected['End']
        assert actual.resolution['value'] == expected['Resolution']['value']


def get_results(culture, model, source):
    return MODELFUNCTION[model](source, culture)
