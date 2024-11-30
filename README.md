# CQQCH - Cariño Qué Quieres Comer Hoy 🍽️

CQQCH es una aplicación desarrollada en Android Studio que tiene como objetivo ayudar a los usuarios a organizar sus restaurantes favoritos, recetas y opciones para cocinar o pedir comida. Es una solución ideal para quienes desean tomar decisiones rápidas y centralizar toda la información relacionada con la comida en un solo lugar.

---

## 🚀 **Características principales**

### 🏢 **1. Gestión de Restaurantes**
- Los usuarios pueden agregar restaurantes con los siguientes detalles:
  - **Nombre** 🏷️
  - **Dirección** 📍
  - **Nota** (puntuación del 1 al 5) ⭐
  - **Categoría** 🍴 (como Pasta, Carne, Vegetariano, etc.)
  - **¿Se puede ir?** ✅ (Sí o No)
  - **¿Se puede pedir?** 🛵 (Sí o No)
  - **Precio aproximado** 💰
  - **Comentarios adicionales** 📝

- Los datos se almacenan en Firebase para asegurar persistencia y sincronización en diferentes dispositivos.

### 📋 **2. Visualización de Restaurantes**
- Los usuarios pueden ver una lista de los restaurantes que han añadido, con todos los detalles visibles:
  - Categoría seleccionada.
  - Indicaciones claras de "¿Se puede ir?" y "¿Se puede pedir?" según las opciones seleccionadas en los desplegables.


### 🍳 **3. Gestión de Recetas**
Los usuarios pueden agregar recetas con los siguientes detalles:
  - **Nombre** 🏷️
  - **Categoría** 🍴 (como Pasta, Carne, Pescado, Vegetariano, etc.)
  - **Preparación** 📍
  - **Nota** (puntuación del 1 al 5) ⭐
  - **Precio** 💰
  - **Tiempo** 📝

    
### 📋 4. Visualización de Recetas
Los usuarios pueden ver una lista de los restaurantes que han añadido, con todos los detalles visibles:
Categoría seleccionada.
Indicaciones claras de los campos introducidos.


### 🧭 **5. Navegación Intuitiva**
- Botones de navegación como "Volver al Menú" permiten a los usuarios desplazarse entre las pantallas de forma fluida.


---

## 🌟 **Próximas Funcionalidades**

### ❤️ **1. Sistema de Favoritos**
- **¿Qué es?**
  - Los usuarios podrán marcar un restaurante o recetas como favorito mediante un icono de corazón.
  - Restaurantes favoritos o recetas tendrán un icono de corazón relleno (♥), mientras que los no favoritos tendrán un corazón vacío (♡).


- **Cómo funcionará:**
  - Desde la lista de restaurantes, el usuario podrá pulsar sobre el icono de corazón. Se hará lo mismo con el apartado de Cocinar (Recetas)
  - Al marcar un restaurante como favorito, al igual que al marcar una Receta. Se almacenará en Firebase.
  - Habrá un apartado exclusivo para ver solo los restaurantes y recetas marcados como favoritos.

### 🎲 **2. Funcionalidad de "Cariño, elige tú"**
- Una herramienta que ayudará a los usuarios indecisos a elegir un restaurante o receta según filtros.

---

## 🛠️ **Tecnologías Utilizadas**

- **Lenguaje de programación:** Java
- **IDE:** Android Studio
- **Base de datos:** Firebase Realtime Database
- **Diseño de UI:** XML con soporte para `RecyclerView` y `ConstraintLayout`

---

