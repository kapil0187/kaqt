#!/usr/bin/python3

import numpy as np
import pandas as pd
import datetime

import tkinter as tk
from tkinter import ttk

import matplotlib
matplotlib.use("TkAgg")

from matplotlib import pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg, NavigationToolbar2TkAgg
from matplotlib.figure import Figure
import matplotlib.animation as animation
from matplotlib import style
from matplotlib import gridspec as gs
import matplotlib.dates as mdates
import matplotlib.ticker as mticker


LARGE_FONT = ("Verdana", 12)
DARK_COLOR = "#183A54"
LIGHT_COLOR = "#00A3E0"
mode = 'spread'

style.use(['ggplot'])

fig = plt.Figure()
gridspec = gs.GridSpec(8, 4)


price_ax = fig.add_subplot(gridspec.new_subplotspec([0,0], rowspan=6, colspan=4))

if mode == 'spread':
    spread_ax = fig.add_subplot(gridspec.new_subplotspec([6,0], rowspan=1, colspan=4), sharex=price_ax)
    positions_ax = fig.add_subplot(gridspec.new_subplotspec([7,0], rowspan=1, colspan=4), sharex=price_ax)
elif mode == 'bidask':
    bid_vol_ax = fig.add_subplot(gridspec.new_subplotspec([6,0], rowspan=1, colspan=4), sharex=price_ax)
    ask_vol_ax = fig.add_subplot(gridspec.new_subplotspec([7,0], rowspan=1, colspan=4), sharex=price_ax)
elif mode == 'mid':
    positions_ax = fig.add_subplot(gridspec.new_subplotspec([6, 0], rowspan=2, colspan=4), sharex=price_ax)


initial_timedelta = 100000
total_size = 100000

symbol1 = 'GEZ5'
symbol2 = 'GEH6'

start_date = pd.datetime.now() - datetime.timedelta(days=initial_timedelta)
index_vals = pd.date_range(start=start_date,
                           end=start_date+datetime.timedelta(seconds=(total_size - 1)),
                           freq='S')



prices = pd.DataFrame()

if mode == 'spread':
    prices_symbol1 = 100 + pd.Series(np.random.randn(total_size)).cumsum()
    prices_symbol2 = 2 * np.abs(np.random.randn(1)) + \
                     pd.Series(np.random.random_integers(5, 10, total_size)) + prices_symbol1

    prices = pd.DataFrame({symbol1: prices_symbol1,
                           symbol2: prices_symbol2,
                           'Spread': prices_symbol2 - prices_symbol1})

    prices.index = index_vals

    prices['SMA_Short'] = pd.rolling_mean(prices['Spread'], window=50)
    prices['SMA_Long'] = pd.rolling_mean(prices['Spread'], window=200)
    prices['ShortEnter'] = prices['SMA_Short'] > prices['SMA_Long']
    prices['LongEnter'] = prices['SMA_Short'] < prices['SMA_Long']
    prices['ShortPosition'] = 0
    prices['LongPosition'] = 0
    prices.ix[prices.ShortEnter, 'ShortPosition'] = -1
    prices.ix[prices.LongEnter, 'LongPosition'] = 1


elif mode == 'bidask':
    prices_bid = 100 + 0.01 * pd.Series(np.random.random_integers(1, 3, total_size))
    prices_ask = 2 * np.abs(np.random.randn(1)) + \
                     0.01 * pd.Series(np.random.random_integers(5, 10, total_size)) + prices_bid
    bid_quantity = pd.Series(np.random.random_integers(0, 3, total_size))
    ask_quantity = pd.Series(np.random.random_integers(0, 3, total_size))
    prices = pd.DataFrame({'Bid': prices_bid,
                           'Ask': prices_ask,
                           'BidSize': bid_quantity,
                           'AskSize': ask_quantity})
    prices.index = index_vals

elif mode == 'mid':
    prices_bid = 100 + pd.Series(np.random.randn(total_size)).cumsum()
    prices_ask = 2 * np.abs(np.random.randn(1)) + \
                     0.01 * pd.Series(np.random.random_integers(5, 10, total_size)) + prices_bid

    prices = pd.DataFrame({'Bid': prices_bid,
                           'Ask': prices_ask,
                           'Mid': (prices_bid + prices_bid)/2})

    prices.index = index_vals

    prices['SMA_Short'] = pd.rolling_mean(prices['Mid'], window=50)
    prices['SMA_Long'] = pd.rolling_mean(prices['Mid'], window=200)
    prices['ShortEnter'] = prices['SMA_Short'] > prices['SMA_Long']
    prices['LongEnter'] = prices['SMA_Short'] < prices['SMA_Long']
    prices['ShortPosition'] = 0
    prices['LongPosition'] = 0
    prices.ix[prices.ShortEnter, 'ShortPosition'] = -1
    prices.ix[prices.LongEnter, 'LongPosition'] = 1


incr = 0
update_freq = 100


def animate(i):
    global prices
    global incr
    global start_date
    if mode == 'spread':
        prices_to_plot = prices.ix[incr:incr + update_freq, [symbol1, symbol2]]
        spread_to_plot = prices.ix[incr:incr + update_freq, ['Spread', 'SMA_Long', 'SMA_Short']]
        positions_to_plot = prices.ix[incr:incr + update_freq, ['ShortPosition', 'LongPosition']].sum(axis=1)

        incr += 1
        price_ax.clear()
        spread_ax.clear()
        positions_ax.clear()

        price_ax.plot_date(prices_to_plot.index.values, prices_to_plot[symbol1],
                           DARK_COLOR, label=symbol1)
        price_ax.plot_date(prices_to_plot.index.values, prices_to_plot[symbol2],
                           LIGHT_COLOR, label=symbol2)
        price_ax.set_ylabel('Prices')

        plt.setp(price_ax.get_xticklabels(), visible=False)

        spread_ax.plot(spread_to_plot.index.values, spread_to_plot['Spread'],
                       color='maroon', label='Spread')
        spread_ax.plot(prices_to_plot.index.values, spread_to_plot['SMA_Short'],
                       color='g', label='SMA Short')
        spread_ax.plot(prices_to_plot.index.values, spread_to_plot['SMA_Long'],
                       color='b', label='SMA Long')
        spread_ax.set_ylabel('Spreads')
        plt.setp(spread_ax.get_xticklabels(), visible=False)

        positions_ax.plot(positions_to_plot.index.values, positions_to_plot.values,
                          color='black', label='Position')

        positions_ax.set_ylim([-2, 2])
        positions_ax.set_ylabel('Positions')
        positions_ax.xaxis.set_major_locator(mticker.MaxNLocator(5))
        positions_ax.xaxis.set_major_formatter(mdates.DateFormatter("%H:%M:%S"))
        positions_ax.set_yticks((-1, 0, 1))
        positions_ax.set_yticklabels(('-1', '0', '+1'))

        price_ax.legend(bbox_to_anchor=(0, 1.02, 1, .102), loc=3, ncol=2, borderaxespad=0)

        title = 'Simulated Market Data and Strategy for Spread\n' + symbol1 + "-" + symbol2
        price_ax.set_title(title)
    elif mode == 'bidask':
        prices_to_plot = prices.ix[incr:incr + update_freq, ['Bid', 'Ask']]
        vol_to_plot = prices.ix[incr:incr + update_freq, ['BidSize', 'AskSize']]

        incr += 1
        price_ax.clear()
        bid_vol_ax.clear()
        ask_vol_ax.clear()

        price_ax.plot_date(prices_to_plot.index.values, prices_to_plot['Bid'],
                           DARK_COLOR, label='Bid')
        price_ax.plot_date(prices_to_plot.index.values, prices_to_plot['Ask'],
                           LIGHT_COLOR, label='Ask')
        plt.setp(price_ax.get_xticklabels(), visible=False)

        bid_vol_ax.fill_between(vol_to_plot.index.values,
                                0, vol_to_plot.BidSize, facecolor='cyan')

        bid_vol_ax.xaxis.set_major_locator(mticker.MaxNLocator(5))
        bid_vol_ax.xaxis.set_major_formatter(mdates.DateFormatter("%H:%M:%S"))
        plt.setp(bid_vol_ax.get_xticklabels(), visible=False)

        ask_vol_ax.fill_between(vol_to_plot.index.values,
                                0, vol_to_plot.AskSize, facecolor='maroon')

        ask_vol_ax.xaxis.set_major_locator(mticker.MaxNLocator(5))
        ask_vol_ax.xaxis.set_major_formatter(mdates.DateFormatter("%H:%M:%S"))

        price_ax.legend(bbox_to_anchor=(0, 1.02, 1, .102), loc=3, ncol=2, borderaxespad=0)

        title = 'Simulated Market Data Feed'
        price_ax.set_title(title)

    if mode == 'mid':
        prices_to_plot = prices.ix[incr:incr + update_freq, ['Mid', 'SMA_Short', 'SMA_Long']]
        positions_to_plot = prices.ix[incr:incr + update_freq, ['ShortPosition', 'LongPosition']].sum(axis=1)

        incr += 1
        price_ax.clear()
        positions_ax.clear()

        price_ax.plot_date(prices_to_plot.index.values, prices_to_plot['Mid'],
                           'blue', label='Mid')
        price_ax.plot_date(prices_to_plot.index.values, prices_to_plot['SMA_Short'],
                           DARK_COLOR, label='SMA_Short')
        price_ax.plot_date(prices_to_plot.index.values, prices_to_plot['SMA_Long'],
                           LIGHT_COLOR, label='SMA_Long')

        plt.setp(price_ax.get_xticklabels(), visible=False)

        positions_ax.plot(positions_to_plot.index.values, positions_to_plot.values,
                          color='black', label='Position')

        positions_ax.set_ylim([-2, 2])
        positions_ax.set_ylabel('Positions')
        positions_ax.xaxis.set_major_locator(mticker.MaxNLocator(5))
        positions_ax.xaxis.set_major_formatter(mdates.DateFormatter("%H:%M:%S"))
        positions_ax.set_yticks((-1, 0, 1))
        positions_ax.set_yticklabels(('-1', '0', '+1'))

        price_ax.legend(bbox_to_anchor=(0, 1.02, 1, .102), loc=3, ncol=2, borderaxespad=0)

        title = 'Simulated Market Data Feed and Strategy'
        price_ax.set_title(title)


class TicksPlayer(tk.Tk):
    def __init__(self, *args, **kwargs):
        tk.Tk.__init__(self, *args, **kwargs)

        tk.Tk.wm_title(self, 'Ticks Player')

        container = tk.Frame(self)

        container.pack(side='top', fill='both', expand=True)
        container.grid_rowconfigure(0, weight=1)
        container.grid_columnconfigure(0, weight=1)

        self.frames = {}

        for f in [ChartingFrame]:
            frame = f(container, self)
            self.frames[f] = frame
            frame.grid(row=0, column=0, sticky='nsew')

        self.show_frame(ChartingFrame)

    def show_frame(self, frame_type):
        frame = self.frames[frame_type]
        frame.tkraise()


class ChartingFrame(tk.Frame):
    def __init__(self, parent, controller):
        tk.Frame.__init__(self, parent)

        canvas = FigureCanvasTkAgg(fig, self)
        canvas.show()
        canvas.get_tk_widget().pack(side=tk.TOP, fill=tk.BOTH, expand=True)
        canvas._tkcanvas.pack(side=tk.TOP, fill=tk.BOTH, expand=True)


def main():
    app = TicksPlayer()
    app.geometry('1280x720')
    ani = animation.FuncAnimation(fig, func=animate, interval=200, blit=False)
    app.mainloop()

if __name__ == '__main__':
    main()


