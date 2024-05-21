import configparser

from flask import Flask, request
import shioaji as sj
import pandas as pd

api = None


class ShiaojiUtil():
    def __init__(self):
        config = configparser.ConfigParser()
        config.read('../application.ini')
        self.api = sj.Shioaji()
        self.api.login(
            config["DEFAULT"]["shiaoji_api_key"],
            config["DEFAULT"]["shiaoji_secret_key"]
        )

    # 內外盤委託單(2330, 2024-04-21)
    def ticks(self, stock_code, date):
        ticks = self.api.ticks(self.api.Contracts.Stocks[stock_code], date)
        df = pd.DataFrame({**ticks})
        df.ts = pd.to_datetime(df.ts)
        return df

    # 分K("2330", "2024-04-21", "2024-04-21")
    def kbar(self, stock_code, start_date, end_date):
        kbar = self.api.kbars(
            contract=self.api.Contracts.Stocks[stock_code],
            start=start_date,
            end=end_date,
        )
        df = pd.DataFrame({**kbar})
        df.ts = pd.to_datetime(df.ts)
        return df


if __name__ == '__main__':
    shiaoji = ShiaojiUtil()
    # print(shiaoji.ticks("2330", "2024-05-21"))
    # print(shiaoji.kbar("2330", "2024-05-21", "2024-05-21"))
