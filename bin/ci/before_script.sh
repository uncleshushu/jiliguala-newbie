#!/usr/bin/env bash
cd resources/private
gpg --batch --passphrase $GPG_PASSPHRASE -o conf.tar -d conf.tar.gpg
tar -xf conf.tar && rm -f *.tar *.gpg
