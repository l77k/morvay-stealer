@echo off
chcp 65001 >nul

REM === GitHub repo adresin ===
set REPO_URL=https://github.com/l77k/xxx.git

REM Scriptin oldugu klasore gec
cd /d %~dp0

REM Git yoksa baslat
if not exist .git (
    git init
)

git add .

git commit -m "auto upload"

git branch -M main

git remote remove origin 2>nul
git remote add origin %REPO_URL%

git push -u origin main --force

pause
