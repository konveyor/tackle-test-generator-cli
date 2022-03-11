import tkltest
from setuptools import setup, find_packages

setup(
    name='tkltest',
    version=tkltest.__version__,
    description='Command-line interface for generating and executing test cases on'
                'two application versions and performing differential testing',
    packages=find_packages(),
    install_requires=[
        'coverage==5.5',
        'nose==1.3.7',
        'PyYAML>=5.4',
        'tabulate==0.8.9',
        'textile==4.0.1',
        'toml==0.10.2',
        'yattag==1.14.0',
        'jinja2==3.0.2',
        'bs4==0.0.1',
        'kaitaistruct==0.9',
        'psutil==5.9.0',
        'tqdm==4.62.3'
    ],
    entry_points={
        "console_scripts": [
            "tkltest-unit = tkltest.tkltest_unit:main",
            "tkltest-ui = tkltest.tkltest_ui:main"
        ]
    },
    package_data={
        'tkltest/generate/ui/templates': ['tkltest/generate/ui/templates/*.jinja']
    },
    classifiers=[
        'Programming Language :: Python :: 3',
        'License :: OSI Approved :: EPL-2.0',
        'Operating System :: OS Independent'
    ]
)
