import tkltest
from setuptools import setup, find_packages
from setuptools.command.install import install


class TkltestInstall(install):
    """Install and download nltk data"""
    def run(self):
        install.run(self)
        import nltk
        nltk.download('stopwords')
        nltk.download('averaged_perceptron_tagger')
        nltk.download('wordnet')
        nltk.download('words')
        nltk.download('omw-1.4')


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
        'tqdm==4.62.3',
        'nltk>=3.6.6',
        'pyenchant==3.2.2',
        'lxml==4.9.1',
        'xmltodict==0.12.0',
        'keybert==0.4.0',
        'pandas==1.4.3'

    ],
    setup_requires=['nltk==3.6.6'],
    cmdclass={'install': TkltestInstall},
    entry_points={
        "console_scripts": [
            "tkltest-unit = tkltest.tkltest_unit:main",
            "tkltest-ui = tkltest.tkltest_ui:main"
        ]
    },
    include_package_data=True,
    classifiers=[
        'Programming Language :: Python :: 3',
        'License :: OSI Approved :: APL-2.0',
        'Operating System :: OS Independent'
    ]
)
