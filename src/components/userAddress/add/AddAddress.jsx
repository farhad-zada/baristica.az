import React, { useEffect, useState } from 'react'
import pageText from '../../../content/PagesText.json'
import { useSelector } from 'react-redux';
import styles from './addAddress.module.css'
import InputText from '../../form/inputField/InputText';
import UserService from '../../../services/user.service';
import Loading from '../../../components/loading/Loading'
import Error from '../../error/Error';
const { profile } = pageText

export default function AddAddress({ setAddresses, setAdd }) {
    const { lang, token } = useSelector((state) => state.baristica);

    // ask Farhad to return me object or entire array in response of adding address to set live
    const [formData, setFormData] = useState({
        id: 3,
        city: "",
        street: "",
        apartment: "",
    })
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState(false)

    const userService = new UserService()

    const handleInputChange = (name, value) => {
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    const handleUpdateAddress = async (formData) => {
        setLoading(true)
        try {
            const data = {
                "address": {
                    ...formData,
                    "isPrimary": false
                }
            }
            const response = await userService.addAddress(token, data)
            setAddresses((prevState) => [...prevState, formData]);
            setAdd(false)

        } catch (error) {
            setError(true)
        } finally {
            setLoading(false)
        }
    };

    const onSubmit = async () => {
        await handleUpdateAddress(formData)
    }

    useEffect(() => {
        return () => {
            setFormData({
                id: 3,
                city: "",
                street: "",
                apartment: "",
            })
        }
    }, [])

    return (
        <div className={styles.add}>
            <Loading status={loading} />
            <Error status={error} setStatus={setError} />

            <h2 className={styles.heading + " f28 fw600 darkGrey_color robotoFont"}>{lang ? profile[lang].addresses.newAddress : ''}</h2>

            <form action="">
                <InputText
                    name="city"
                    value={formData.city}
                    onChange={handleInputChange}
                    placeholder={lang ? profile[lang].addresses.cityInput : ''}
                />

                <InputText
                    name="street"
                    value={formData.street}
                    onChange={handleInputChange}
                    placeholder={lang ? profile[lang].addresses.streetInput : ''}
                />

                <InputText
                    name="apartment"
                    value={formData.apartment}
                    onChange={handleInputChange}
                    placeholder={lang ? profile[lang].addresses.houseInput : ''}
                />


                <button
                    type='button'
                    className={styles.saveBtn}
                    onClick={onSubmit}
                >
                    {lang ? profile[lang].addresses.saveBtn : ''}
                </button>
            </form>
        </div>
    )
}
