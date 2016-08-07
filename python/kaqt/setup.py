__author__ = 'ksharma'

from setuptools import setup


def readme():
    with open('README.rst') as f:
        return f.read()

setup(
    name='kaqt',
    version='0.1.9',
    description='K(arma) Quantitative Trading Backtesting',
    long_description='High performnace quantitative trading and backtesting tool',
    url='https://github.com/kapil0187/kaqt.git',
    author='Kapil Sharma',
    author_email='ksharma@dvtglobal.com',
    license='GPL',
    packages=['kaqt'],
    install_requires=[
        'pandas',
        'numpy',
        'pysftp',
        'statsmodels'
    ],
    entry_points={
        'console_scripts': ['download-tickdata=kaqt.command_line.download_tickdata:main',
                            'download-settles=kaqt.command_line.download_settles:main',
                            'ticks2hdf5=kaqt.command_line.ticks2hdf5:main',
                            'settles2hdf5=kaqt.command_line.settles2hdf5:main'],
    },
    zip_safe=False
)