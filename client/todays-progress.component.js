import progressInputComponent from "./progress-input.component";
import "./todays-progress.component.less";

module.exports.default = angular
  .module("todaysProgress", [progressInputComponent.default])
  .component("todaysProgress", {
    bindings: {},
    controller: TodaysProgress,
    template: require("./todays-progress.component.html"),
  }).name;

TodaysProgress.$inject = ["$resource", "$timeout"];
function TodaysProgress($resource, $timeout) {
  let ctrl = this;
  let Books = $resource("/books/:bookName", null, {
    addBookProgress: { method: "PUT", isArray: false },
  });

  ctrl.$onInit = () => {
    queryBooks();
    ctrl.progressUpdating = false;
    ctrl.bookWithRequestConfirmation = null;
  };

  function queryBooks() {
    ctrl.books = Books.query();
    updateFirstLetters();
  }

  ctrl.toggleInputActiveFor = (book, shouldBeActive) => {
    if (shouldBeActive) {
      ctrl.inputsActiveOn = book;
    } else {
      ctrl.inputsActiveOn = null;
    }
  };

  ctrl.nameToHexColor = (name) => {
    let hash = 0;
    for (let i = 0; i < name.length; i++) {
      hash = name.charCodeAt(i) + ((hash << 5) - hash);
    }
    let color = "#";
    for (let i = 0; i < 3; i++) {
      let value = (hash >> (i * 8)) & 0xff;
      color += `00${value.toString(16)}`.substr(-2);
    }
    let transparency10Percent = `33`; // cudos to @lopspower https://gist.github.com/lopspower/03fb1cc0ac9f32ef38f4
    return `${color}${transparency10Percent}`;
  };

  ctrl.updateBookProgress = (bookName, currentPage) => {
    if (!angular.isDefined(currentPage)) return;

    ctrl.progressUpdating = true;
    Books.addBookProgress(
      { bookName: bookName },
      { currentPage: currentPage },
      () => {
        ctrl.progressUpdating = false;
        ctrl.bookWithRequestConfirmation = bookName;
        $timeout(() => (ctrl.bookWithRequestConfirmation = null), 8000);
        queryBooks();
      }
    );
  };

  ctrl.progressInPercent = (book) => {
    return Math.min(
      (book.currentPage / book.pagesTotal) * 100,
      book.pagesTotal
    );
  };

  ctrl.onlyUnfinishedBooks = (book) => {
    if (!angular.isDefined(book)) return false;

    return book.currentPage < book.pagesTotal;
  };

  ctrl.onlyCompletedBooks = (book) => {
    if (!angular.isDefined(book)) return false;

    return book.currentPage === book.pagesTotal;
  };

  function updateFirstLetters() {
    ctrl.books.$promise.then((books) => {
      // format: { "A": ["A book", "Anarchy book"], "B": [...] }
      const _indexedBooks = books.reduce((acc, book) => {
        const firstLetter = book.name.charAt(0);
        if (acc[firstLetter] === undefined) {
          acc[firstLetter] = [book];
        } else {
          acc[firstLetter].push(book);
        }
        return acc;
      }, {});

      ctrl.indexedBooks = Object.entries(_indexedBooks)
        .sort(([firstLetterA, _], [firstLetterB, __]) =>
          firstLetterA.localeCompare(firstLetterB)
        )
        .map(([firstLetter, books]) => {
          return [
            { type: "letter", value: firstLetter },
            ...books.map((book) => ({ type: "book", value: book })),
          ];
        })
        .flat();
    });
  }
}
