call runTaskApp.bat
if "%ERRORLEVEL%" == "0" goto runopera
echo.
echo Error running runTaskApp.bat
goto fail

:runopera
start chrome http://localhost:8080/TaskApp/v1/task/getTasks
goto end

:fail
echo.
echo There were errors

:end
echo.
echo Work is finished


