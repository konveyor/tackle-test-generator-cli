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
        'yattag==1.14.0'
    ],
    entry_points={
        "console_scripts": [
            "tkltest = tkltest.tkltest:main"
        ]
    },
    classifiers=[
        'Programming Language :: Python :: 3',
        'License :: OSI Approved :: EPL-2.0',
        'Operating System :: OS Independent'
    ]
)
