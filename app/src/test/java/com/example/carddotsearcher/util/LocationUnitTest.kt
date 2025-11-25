
package com.example.carddotsearcher.util

import org.junit.Assert.assertEquals
import org.junit.Test

class LocationUtilsTest {

    @Test
    fun calculateDistance_con_coordenadas_conocidas_devuelve_distancia_correcta() {
        // 1. ARRANGE (Preparar)
        // Coordenadas de Santiago, Chile a Buenos Aires, Argentina (aproximadas)
        val santiagoLat = -33.4489
        val santiagoLon = -70.6693
        val buenosAiresLat = -34.6037
        val buenosAiresLon = -58.3816

        // La distancia esperada es aproximadamente 1139 km.
        val expectedDistanceKm = 1139f

        // 2. ACT (Actuar)
        val actualDistanceKm = calculateDistance(santiagoLat, santiagoLon, buenosAiresLat, buenosAiresLon)

        // 3. ASSERT (Verificar)
        // Verificamos si el resultado es cercano al esperado.
        // Usamos un 'delta' de 10km para permitir pequeñas variaciones en el cálculo.
        assertEquals(
            "La distancia calculada no es la esperada",
            expectedDistanceKm,
            actualDistanceKm,
            10f // Aceptamos una diferencia de hasta 10km
        )
    }
}
    