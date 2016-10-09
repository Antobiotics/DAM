#!/bin/bash

echo "Setting up environment from: ${PWD}"

DAM_ENV_FILE=${PWD}/"dam_env.sh"
DAM_ENV_TEMPLATE=${PWD}/${DAM_ENV_FILE}".template"

DAM_CONF_ENV_FILE="${PWD}/src/main/scala/ressources/dam.conf"

if [ ! -f "${DAM_ENV_FILE}" ]; then
	echo "Missing dam_env.sh, creating one"
	cp "${DAM_ENV_TEMPLATE}" "${DAM_ENV_FILE}"
	chmod +x "${DAM_ENV_FILE}"
	echo "Please update: ${DAM_ENV_FILE}"
	exit 1
fi

source "${DAM_ENV_FILE}"
envsubst < "${DAM_CONF_ENV_FILE}"".template" > "${DAM_CONF_ENV_FILE}"


