function rob_dp=RV(rawphi,rawstr,rawA,rawb,rawclg,rawtime)
phi=char(rawphi);
str=strsplit(rawstr,',');
d=cell2mat(str(1));
Pred(1).str=d;
e=cell2mat(str(2));
Pred(2).str=e;
f=cell2mat(str(3));
Pred(3).str=f;

h1=zeros(1,3);
h2=zeros(1,3);
h3=zeros(1,3);

a=strsplit(rawA,',');
g=cell2mat(a(1));
h1(1)=str2num(g);
x=cell2mat(a(2));
h2(2)=str2num(x);
y=cell2mat(a(3));
h3(3)=str2num(y);
Pred(1).A=h1;
Pred(2).A=h2;
Pred(3).A=h3;

b1=zeros(1,3);
b2=zeros(1,3);
b3=zeros(1,3);

b=strsplit(rawb,',');
g1=cell2mat(b(1));
b1(1)=str2num(g1);
x1=cell2mat(b(2));
b2(2)=str2num(x1);
y1=cell2mat(b(3));
b3(3)=str2num(y1);
Pred(1).b=b1;
Pred(2).b=b2;
Pred(3).b=b3;

Pred(1).loc=1;
Pred(2).loc=2;
Pred(3).loc=3;

time=rawtime.';
clg=rawclg.';
rob_dp=dp_taliro(phi,Pred,clg,time);
end
