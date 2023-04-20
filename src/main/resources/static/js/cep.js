const $ = document.querySelector.bind(document)

const cepField = $('#cep')

const validaCEP = cep => {
    const regexCep = /^[0-9]{8}$/;

    return regexCep.test(cep);
}

const consultaCEP = async cep => {
    const URL = 'https://viacep.com.br/ws/'

    if (! validaCEP(cep)) {
        console.log('O CEP não é válido')
        setFieldDate('')
        return
    }

    setFieldDate('Carregando dados...')

    await fetch(URL + cep + '/json/')
        .then(response => response.json())
        .then(data => {
            setFieldDate(data)
        })
        .catch(error => {
            console.error(error)
            setFieldDate('')
        })
}

const setFieldDate = data => {
    const { logradouro, complemento, bairro, localidade, uf, ibge } = data

    $('#rua').value = logradouro ?? data
    $('#complemento').value = complemento ?? data
    $('#bairro').value = bairro ?? data
    $('#cidade').value = localidade ?? data
    $('#uf').value = uf ?? data
    $('#ibge').value = ibge ?? data
}

if (cepField)
    cepField.addEventListener('blur', event => consultaCEP(event.target.value))
