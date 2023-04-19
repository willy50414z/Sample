import 'package:decimal/decimal.dart';

class InvestPlanRowDto {
  Decimal? _amt;
  Decimal? _index;

  InvestPlanRowDto(amt, index) {
    _amt = amt;
    _index = index;
  }

  Decimal? get amt => _amt;
  set amt(Decimal? value) => _amt = value;

  Decimal? get index => _index;
  set index(Decimal? value) => _index = value;
}