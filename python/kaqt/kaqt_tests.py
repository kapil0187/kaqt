import unittest as ut
import pandas as pd
import datetime
from kaqt.backtest.metrics import PivotPoints


class PivotPointsTest(ut.TestCase):
    def setUp(self):
        to_test = pd.DataFrame({'High': 200,
                                'Low': 100,
                                'Open': 120,
                                'Close': 180},
                               index=pd.Series([datetime.datetime.now()]))
        self.pp_ = PivotPoints(to_test)

    def test_standard(self):
        sp = self.pp_.standard()
        self.assertEqual(sp['PP'].values[0], 160)
        self.assertEqual(sp['R1'].values[0], 220)
        self.assertEqual(sp['R2'].values[0], 260)
        self.assertEqual(sp['R3'].values[0], 360)
        self.assertEqual(sp['R4'].values[0], 460)
        self.assertEqual(sp['S1'].values[0], 120)
        self.assertEqual(sp['S2'].values[0], 60)
        self.assertEqual(sp['S3'].values[0], -40)
        self.assertEqual(sp['S4'].values[0], -140)

    def test_camarilla(self):
        cp = self.pp_.camarilla()
        self.assertEqual(cp['R1'].values[0], 180 + 110/12)
        self.assertEqual(cp['R2'].values[0], 180 + 55/3)
        self.assertEqual(cp['R3'].values[0], 207.5)
        self.assertEqual(cp['R4'].values[0], 235)
        self.assertEqual(cp['S1'].values[0], 180 - 110/12)
        self.assertEqual(cp['S2'].values[0], 180 - 55/3)
        self.assertEqual(cp['S3'].values[0], 152.5)
        self.assertEqual(cp['S4'].values[0], 125)

    def test_woodie(self):
        wp = self.pp_.woodie()
        self.assertEqual(wp['PP'].values[0], 135)
        self.assertEqual(wp['R1'].values[0], 170)
        self.assertEqual(wp['R2'].values[0], 235)
        self.assertEqual(wp['R3'].values[0], 270)
        self.assertEqual(wp['R4'].values[0], 370)
        self.assertEqual(wp['S1'].values[0], 70)
        self.assertEqual(wp['S2'].values[0], 35)
        self.assertEqual(wp['S3'].values[0], -30)
        self.assertEqual(wp['S4'].values[0], -130)

    def test_floor(self):
        fp = self.pp_.floor()
        self.assertEqual(fp['PP'].values[0], 160)
        self.assertEqual(fp['R1'].values[0], 220)
        self.assertEqual(fp['R2'].values[0], 260)
        self.assertEqual(fp['R3'].values[0], 300)
        self.assertEqual(fp['S1'].values[0], 120)
        self.assertEqual(fp['S2'].values[0], 60)
        self.assertEqual(fp['S3'].values[0], 20)

    def test_demark(self):
        mp = self.pp_.demark()
        self.assertEqual(mp['PP'].values[0], 170)
        self.assertEqual(mp['R1'].values[0], 240)
        self.assertEqual(mp['S1'].values[0], 140)

if __name__ == '__main__':
    ut.main()



