# calculator
<p>
Simple ariphmetic expression calculator.
Based on traning project "Smart Calculator" by Hyperskill https://hyperskill.org/projects/42

<p>
I used a recursive parser based on the following grammar
<li>    expression -> add [EOF]</li>
<li>    add -> mult ([+|-] mult)*</li>
<li>    mult -> group ([*|/] group)*</li>
<li>    group   -> [(] add [)] | number</li>
<li>    number -> ([+|-])* [double | variable]</li>
<li>    double -> "[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?"</li>
<li>    variable -> "[a-zA-Z]+"</li>
    
<p>
All calculations are performed on double values. 
All spacers in the input are ignored.

<p> 
The program supports variables. The variable name consists of Latin characters without spaces.
Use the following syntax to assign a value to a variable:
  VARIABLE = expression

<p>  
Some valid assignments:
<li>    a = 2</li>
<li>    a=  (2+2)/2</li>
<li>    first = 1/2</li>
<li>    second = 0.5</li>
<li>    result = (first+second) *2
  
Type variable name to print its value.
