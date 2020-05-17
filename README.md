# calculator
Simple ariphmetic expression calculator.
Based on traning project "Smart Calculator" by Hyperskill https://hyperskill.org/projects/42

I used a recursive parser based on the following grammar
    expression -> add [EOF]
    add -> mult ([+|-] mult)*
    mult -> group ([*|/] group)*
    group   -> [(] add [)] | number
    number -> ([+|-])* [double | variable]
    double -> "[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?"
    variable -> "[a-zA-Z]+"
    
All calculations are performed on double values. 
All spacers in the input are ignored.
    
The program supports variables. The variable name consists of Latin characters without spaces.
Use the following syntax to assign a value to a variable:
  VARIABLE = expression
  
Some valid assignvents:
  a = 2
  a=  (2+2)/2
  first = 1/2
  second = 0.5
  result = (first+second) *2
  
Type variable name to print its value.
