import tkltest
from setuptools import setup, find_packages


setup(
    name='tkltest',
    version=tkltest.__version__,
    description='Command-line interface for generating and executing unit test cases for Java application',
    packages=find_packages(),
    install_requires=[
        'coverage==7.3.0',
        'green==3.4.3',
        'PyYAML==6.0.1',
        'tabulate==0.9.0',
        'textile==4.0.2',
        'toml==0.10.2',
        'yattag==1.15.0',
        'jinja2==3.1.2',
        'beautifulsoup4==4.12.0',
        'kaitaistruct==0.9'
    ],
    entry_points={
        "console_scripts": [
            "tkltest-unit = tkltest.unit.tkltest_unit:main"
        ]
    },
    include_package_data=True,
    classifiers=[
        'Programming Language :: Python :: 3',
        'License :: OSI Approved :: APL-2.0',
        'Operating System :: OS Independent'
    ]
)
