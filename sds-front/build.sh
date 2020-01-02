#!/bin/bash

echo "node version: "`node --version`
echo "npm version: "`npm -version`

npm install
npm run build