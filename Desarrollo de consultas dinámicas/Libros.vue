<template>
  <div style="padding-top: 30px; padding-left: 20px;">
    <div class="d-flex justify-content-between">
      <b-form inline @submit.prevent="buscarPor" class="d-flex align-items-end">
  <label class="mr-sm-2">Filtrar por:</label><br>
  <b-form-select
    class="mb-2 mr-sm-2 mb-sm-0"
    v-model="tipoFiltro"
    :options="['Todo', 'Autor', 'Género', 'Título', 'Año']"
    :value="null"
  ></b-form-select>
  <b-input-group class="mb-2 mr-sm-2 mb-sm-0" v-if="tipoFiltro !== 'Género' && tipoFiltro !== 'Año'">
  <b-form-input v-model="valorFiltro"></b-form-input>
</b-input-group>
<b-input-group class="mb-2 mr-sm-2 mb-sm-0" v-if="tipoFiltro === 'Género'">
  <b-form-select v-model="valorFiltro" :options="options"></b-form-select>
</b-input-group>
  <b-input-group class="mb-2 mr-sm-2 mb-sm-0" v-if="tipoFiltro === 'Año'">
    <b-form-datepicker v-model="añoInicio" class="mb-2"></b-form-datepicker>
    <b-form-datepicker v-model="añoFin" class="mb-2"></b-form-datepicker>
  </b-input-group>
  <b-button type="submit" variant="primary" style="margin-left: 20px;">Buscar</b-button>
</b-form>
      
    </div>
    <b-button pill size="lg" v-b-modal.modal-1 style="background-color: #2169d6; color:" >Agregar Libro</b-button>

    <b-modal id="modal-1" title="Agregar Libro" hide-footer hide-header-close centered>
       <b-form @submit.prevent="onSubmit">
      <b-form-group label="Nombre:" label-for="input-1" label-cols-sm="3" label-align-sm="right" label-size="sm">
        <b-col sm="9">
          <b-form-input id="input-1" v-model="form.nombre" placeholder="Título" required></b-form-input>
        </b-col>
      </b-form-group>

      <b-form-group label="Nombre del Autor:" label-for="input-2" label-cols-sm="3" label-align-sm="right" label-size="sm">
        <b-col sm="9">
          <b-form-input id="input-2" v-model="form.nombreAutor" placeholder="Nombre del Autor" required></b-form-input>
        </b-col>
      </b-form-group>

      <b-form-group label="Género:" label-for="input-3" label-cols-sm="3" label-align-sm="right" label-size="sm">
        <b-col sm="9">
          <b-form-select v-model="form.genero" :options="options"></b-form-select>
        </b-col>
      </b-form-group>

      <b-form-group label="Año de Publicación:" label-for="input-4" label-cols-sm="3" label-align-sm="right" label-size="sm">
        <b-col sm="9">
          <b-form-input id="input-4" v-model="form.fechaPublicacion" type="date" required></b-form-input>
        </b-col>
      </b-form-group>

      <b-row class="mt-3">
        <b-col sm="12" class="text-right">
          <b-button type="submit" variant="primary" style="margin-right: 3px;">Enviar</b-button>
          <b-button variant="secondary" @click="$bvModal.hide('modal-1')" style="margin-left: 3px;">Cancelar</b-button>
        </b-col>
      </b-row>
   
    </b-form>
  </b-modal>

  <div class="d-flex justify-content-center align-items-center">
    <b-spinner v-if="showSpinner" variant="info" style="width: 8rem; height: 8rem; margin-top:15%;"></b-spinner>
  </div>
   

  <b-card-group deck class="getlibros">
    <b-card v-for="(libro, index) in librosFiltrados" :key="index" class="custom-card">
      <template #header>
        <h5 class="card-title">{{ libro.nombre }}</h5>
      </template>
      <b-card-text>
        <p><strong>Nombre del Autor:</strong> {{ libro.nombreAutor }}</p>
        <p><strong>Género:</strong> {{ libro.genero }}</p>
        <p><strong>Año de Publicación:</strong> {{ libro.fechaPublicacion }}</p>
      </b-card-text>
    </b-card>
  </b-card-group>
</div>
</template>

<script>
export default {
  data() {
    return {
      options: [
        { value: null, text: "SELECCIONA UN GÉNERO" },
        { value: "terror", text: "Terror" },
        { value: "novela", text: "Novela" },
        { value: "accion", text: "Acción"},
        { value: "cuento", text: "Cuento"},
        { value: "poesía", text: "Poesía"},
        { value: "humor", text: "Humor"},
        { value: "ficción", text: "Ficción"},
        { value: "libro de autoayuda", text: "Libro de autoayuda"},
      ],
      form: {
        nombre: "",
        nombreAutor: "",
        genero: null,
        fechaPublicacion: null
      },
      libros: [],
      showSpinner: false,
      tipoFiltro: null,
      valorFiltro: "",
      añoInicio: null,
      añoFin: null,
    };
  },
  computed: {
    librosFiltrados() {
      let filtered = this.libros;
      if (this.tipoFiltro === "Autor") {
        filtered = filtered.filter(libro => libro.nombreAutor.toLowerCase().includes(this.valorFiltro.toLowerCase()));
      }  else if (this.tipoFiltro === "Género") {
        filtered = filtered.filter(libro => libro.genero === this.valorFiltro);
      } else if (this.tipoFiltro === "Título") {
        filtered = filtered.filter(libro => libro.nombre.toLowerCase().includes(this.valorFiltro.toLowerCase()));
      } else if (this.tipoFiltro === "Año") {
        filtered = filtered.filter(libro => {
          const libroDate = new Date(libro.fechaPublicacion);
          return libroDate >= new Date(this.añoInicio) && libroDate <= new Date(this.añoFin);
        });
      }
      return filtered;
    },
  },
  mounted() {
    this.spinner(); 
  },
  methods: {
    onSubmit() {
      console.log("Successfully sent!");
      this.$bvModal.hide("modal-1");

      var url = "http://localhost:8080/api/libros";
      var data = this.form;

      fetch(url, {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
          "Content-Type": "application/json",
        },
      })
      .then((res) => res.json())
      .then((response) => {
        console.log("Success:", response);
        this.getLibros();
      })
      .catch((error) => console.error("Error:", error));
    },

    spinner() {
      this.showSpinner = true;
      setTimeout(() => {
        this.showSpinner = false;
        this.getLibros();
      }, 1500);
    },

    getLibros() {
      fetch("http://localhost:8080/api/libros")
        .then((response) => {
          if (!response.ok) {
            throw new Error("");
          }
          return response.json();
        })
        .then((data) => {
          console.log("Success:", data);
          // Convertir la fecha al formato deseado (año-mes-día)
          this.libros = data.map(libro => {
            libro.fechaPublicacion = new Date(libro.fechaPublicacion).toISOString().slice(0, 10);
            return libro;
          });
        })
        .catch((error) => {
          console.error("Error:", error);
        });
    },
    buscarPor() {
  if (this.tipoFiltro === "Año") {
    // Realizar la consulta por año de manera descendente
    fetch(`http://localhost:8080/api/libros?fechaInicio=${this.añoInicio}&fechaFin=${this.añoFin}&orden=desc`)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Error en la solicitud de búsqueda por año.");
        }
        return response.json();
      })
      .then((data) => {
        console.log("Libros encontrados por año de manera descendente:", data);
        // Convertir la fecha al formato deseado (año-mes-día)
        this.libros = data.map(libro => {
          libro.fechaPublicacion = new Date(libro.fechaPublicacion).toISOString().slice(0, 10);
          return libro;
        });
        // Ordenar libros por año de manera descendente
        this.libros.sort((a, b) => new Date(b.fechaPublicacion) - new Date(a.fechaPublicacion));
      })
      .catch((error) => {
        console.error("Error en la búsqueda por año:", error);
      });
  } else if (this.tipoFiltro === "Género") {
    // Realizar la consulta por género
    fetch(`http://localhost:8080/api/libros?genero=${this.valorFiltro}`)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Error en la solicitud de búsqueda por género.");
        }
        return response.json();
      })
      .then((data) => {
        console.log("Libros encontrados por género:", data);
        this.libros = data;
      })
      .catch((error) => {
        console.error("Error en la búsqueda por género:", error);
      });
  } else if (this.tipoFiltro === "Todo") {
    // Realizar la consulta por todos los libros y ordenar por año de manera descendente
    fetch(`http://localhost:8080/api/libros?orden=desc`)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Error en la solicitud de búsqueda de todos los libros.");
        }
        return response.json();
      })
      .then((data) => {
        console.log("Todos los libros encontrados:", data);
        // Convertir la fecha al formato deseado (año-mes-día)
        this.libros = data.map(libro => {
          libro.fechaPublicacion = new Date(libro.fechaPublicacion).toISOString().slice(0, 10);
          return libro;
        });
        // Ordenar libros por año de manera descendente
        this.libros.sort((a, b) => new Date(b.fechaPublicacion) - new Date(a.fechaPublicacion));
      })
      .catch((error) => {
        console.error("Error en la búsqueda de todos los libros:", error);
      });
  } else {
    // Lógica para otros filtros
  }
},
  },
};



</script>

<style scoped>
.getlibros {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-gap: 20px; 
  margin-top: 20px;
}

.custom-card {
  padding: 20px;
  margin-bottom: 20px;
  background-color: #c2edf7; 
  border: 1px solid #dee2e6;
  border-radius: 10px; 
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.card-title {
  font-size: 1.5rem;
  font-weight: bold;
  color: #1577e0;
}
</style>
