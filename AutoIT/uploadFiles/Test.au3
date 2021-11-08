WinWait("File upload","",4);

ControlFocus("[CLASS:#32770","","Edit1")
Sleep(3000)
ControlSetText("[CLASS:#32770","","Edit1","C:\Users\u1102238\Downloads\test.txt")
Sleep(3000)
ControlClick("[CLASS:#32770","","Button1")
