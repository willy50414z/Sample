import 'package:decimal/decimal.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:sias/util/DBUtil.dart';
import 'package:sqflite/sqflite.dart';

import 'dto/InvestPlanRowDto.dart';

class InvestAmtCaculatorPage extends StatefulWidget {
  const InvestAmtCaculatorPage({Key? key}) : super(key: key);

  @override
  _InvestAmtCaculatorPageState createState() => _InvestAmtCaculatorPageState();
}

class _InvestAmtCaculatorPageState extends State<InvestAmtCaculatorPage> {
  TextEditingController _investmentAmountController = TextEditingController();
  TextEditingController _tierDifferenceController = TextEditingController();
  TextEditingController _numberOfTiersController = TextEditingController();
  TextEditingController _targetNameController = TextEditingController();
  TextEditingController _targetCodeController = TextEditingController();
  TextEditingController _highestPriceController = TextEditingController();
  TextEditingController _lowestPriceController = TextEditingController();
  TextEditingController _referencePriceController = TextEditingController();
  TextEditingController _calResultText = TextEditingController();
  String? _selectedMarket;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('投資'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _buildTextField('基本投資金額', _investmentAmountController),
              _buildTextField('級距差額', _tierDifferenceController),
              _buildTextField('級距數量', _numberOfTiersController),
              _buildTextField('標的名稱', _targetNameController),
              _buildTextField('標的代碼', _targetCodeController),
              _buildMarketDropdown(),
              _buildTextField('最高價', _highestPriceController),
              _buildTextField('最低價', _lowestPriceController),
              _buildTextField('基準價', _referencePriceController),
              SizedBox(
                width: double.infinity,
                child: ElevatedButton(
                  onPressed: () {
                    setState(() {
                      // Perform calculation and update result
                      _calculateInvestPlan();
                    });
                  },
                  child: Text('試算'),
                ),
              ),
              SizedBox(
                width: double.infinity,
                child: ElevatedButton(
                  onPressed: () {
                    _saveInvestPlan();
                  },
                  child: Text('匯入策略'),
                ),
              ),
              SizedBox(height: 16.0),
          Container(
            child: TextField(
              controller: _calResultText,
              readOnly: true,
              maxLines: null, // To allow multiple lines
            )
            ),
            ],
          ),
        ),
      ),
    );
  }

  void _saveInvestPlan() async {
    String investPlanStr = _calResultText.text;
    List<String> investPlanRowList =  investPlanStr.split('\r\n');

    if(_selectedMarket == null) {
      Fluttertoast.showToast(
          msg: "請選擇市場別",
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.BOTTOM,
          timeInSecForIosWeb: 1,
          backgroundColor: Colors.red,
          textColor: Colors.white,
          fontSize: 16.0
      );
      return;
    }

    Database db = await DBUtil.getDatabase();
    var result = await db.rawQuery('SELECT MAX(ID) FROM TRACK_TARGET');
    int trackTargetId = int.parse(result[0]['MAX(ID)'].toString()) + 1;

    db.execute('INSERT INTO TRACK_TARGET VALUES (' + trackTargetId.toString() + ', \'' + _targetNameController.text + '\', \'' + _selectedMarket! + '\', \'' + _targetCodeController.text + '\', \'1970-01-01 00:00:00\', 0, 0)');

    investPlanRowList.forEach((rowStr) {
      List<String> cell = rowStr.split(' - ');
      db.execute('insert into TRACK_LIST ( TRACK_TARGET, UP_LIMIT, DN_LIMIT, AMT ) values(' + trackTargetId.toString() + ',\'' + cell[0] + '\',\'' + cell[1] + '\',\'' + cell[2] + '\');');
    });
    //db.close();
  }

  Future<void> _calculateInvestPlan() async {
    List<InvestPlanRowDto> ipList = calInvPlan();
    var sb = StringBuffer();
    for (int i = 1; i < ipList.length; i++) {
      sb.write(ipList[i - 1].index);
      sb.write(" - ");
      sb.write(ipList[i].index);
      sb.write(" - ");
      sb.write(ipList[i].amt);
      sb.write("\r\n");
    }
    _calResultText.text = sb.toString();

  }

  List<InvestPlanRowDto> calInvPlan() {
    Decimal baseInvAmtNum = Decimal.parse(_investmentAmountController.text);
    Decimal levelDiffAmtNum = Decimal.parse(_tierDifferenceController.text);
    int levelCountNum = int.parse(_numberOfTiersController.text);
    Decimal highNum = Decimal.parse(_highestPriceController.text);
    Decimal lowNum = Decimal.parse(_lowestPriceController.text);
    Decimal baseIdxNum = Decimal.parse(_referencePriceController.text);

    final List<InvestPlanRowDto> ipList = [];
    for (int i = 0; i < levelCountNum; i++) {
      int middleLevelStep = levelCountNum ~/ 2;
      Decimal levelAmt = baseInvAmtNum +
          levelDiffAmtNum *
              (i == 0
                  ? Decimal.parse("0.5")
                  : Decimal.parse("2").pow(i - 1).toDecimal());
      Decimal index;
      if (i < middleLevelStep) {
        index = baseIdxNum +
            ((highNum - baseIdxNum) /
                        Decimal.parse((middleLevelStep - 1).toString()))
                    .toDecimal() *
                Decimal.parse((middleLevelStep - i).toString());
      } else if (i == middleLevelStep) {
        index = baseIdxNum;
      } else if (levelCountNum.toInt() - 1 == i) {
        index = Decimal.parse("0");
      } else {
        index = baseIdxNum -
            ((baseIdxNum - lowNum) /
                        Decimal.parse((middleLevelStep - 1).toString()))
                    .toDecimal() *
                Decimal.parse((i - middleLevelStep).toString());
      }
      ipList.add(new InvestPlanRowDto(levelAmt, index));
    }
    return ipList;
  }

  Widget _buildTextField(String label, TextEditingController controller) {
    return TextField(
      controller: controller,
      decoration: InputDecoration(
        labelText: label,
      ),
    );
  }

  Widget _buildMarketDropdown() {
    return DropdownButtonFormField<String>(
      value: _selectedMarket,
      onChanged: (value) {
        setState(() {
          _selectedMarket = value;
        });
      },
      items: ['TWSE', 'TPEX']
          .map((market) => DropdownMenuItem<String>(
                value: market,
                child: Text(market),
              ))
          .toList(),
      decoration: InputDecoration(
        labelText: '市場別',
      ),
    );
  }
}
