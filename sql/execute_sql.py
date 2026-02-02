from __future__ import annotations
import argparse
import os
import subprocess
import sys
from pathlib import Path


def run_psql(sql_path: Path, user: str, host: str, port: int, password: str) -> None:
    env = os.environ.copy()
    env['PGPASSWORD'] = password

    # Build psql command without -d to match calling `psql -U <user> -f <file>`.
    cmd = ['psql', '-U', user, '-h', host, '-p', str(port), '-f', str(sql_path)]

    print(f"\n=== Executing: {sql_path.name} ===")
    print("Command:", ' '.join(cmd))

    try:
        subprocess.run(cmd, check=True, env=env)
        print(f"OK: {sql_path.name}")
    except FileNotFoundError:
        print("Error: `psql` executable not found in PATH.", file=sys.stderr)
        sys.exit(2)
    except subprocess.CalledProcessError as e:
        print(f"Error: psql failed with return code {e.returncode}", file=sys.stderr)
        sys.exit(e.returncode)


def main() -> None:
    parser = argparse.ArgumentParser(description="Run project SQL files against Postgres (sequentially)")
    parser.add_argument('--data-file', default='table_26_janv.sql', help='Data SQL filename located in `sql` folder (default: dataV5-simple.sql)')
    parser.add_argument('--db-user', default='postgres', help='Postgres user (default: postgres)')
    parser.add_argument('--db-name', default='gestion_db', help='Postgres database name (default: gestion_db)')
    parser.add_argument('--host', default='localhost', help='Postgres host (default: localhost)')
    parser.add_argument('--port', default=5432, type=int, help='Postgres port (default: 5432)')
    # parser.add_argument('--port', default=1112, type=int, help='Postgres port (default: 1112)')
    parser.add_argument('--password', default='snow', help='Postgres password (default: snow)')
    # parser.add_argument('--password', default='admin', help='Postgres password (default: admin)')
    parser.add_argument('--skip-views', action='store_true', help='Skip loading views.sql')

    args = parser.parse_args()

    script_dir = Path(__file__).resolve().parent
    repo_root = script_dir.parent
    sql_dir = repo_root / 'sql'

    if not sql_dir.exists() or not sql_dir.is_dir():
        print(f"Error: sql directory not found: {sql_dir}", file=sys.stderr)
        sys.exit(1)

    # List all .sql files in the sql directory
    sql_files = sorted([p for p in sql_dir.glob('*.sql')])
    if not sql_files:
        print(f"No .sql files found in {sql_dir}", file=sys.stderr)
        sys.exit(1)

    def print_sql_list():
        print("Found the following SQL files:")
        for i, p in enumerate(sql_files, start=1):
            print(f"- {i}. {p.name}")

    print_sql_list()

    print("\nInstructions: Enter the number of a file to execute it.\n"
          "Type 'ok' to stop and exit. Type 'list' to show the files again. Type 'all' to execute all files.")

    while True:
        try:
            choice = input('\nEnter number (or ok/list/all): ').strip()
        except (EOFError, KeyboardInterrupt):
            print('\nAborted by user')
            break

        if not choice:
            continue
        if choice.lower() == 'ok':
            print('Done.')
            break
        if choice.lower() == 'list':
            print_sql_list()
            continue
        if choice.lower() == 'all':
            for p in sql_files:
                run_psql(p, args.db_user, args.host, args.port, args.password)
            print('Executed all SQL files.')
            print_sql_list()
            continue

        # allow comma-separated or single number
        entries = [s.strip() for s in choice.split(',') if s.strip()]
        for entry in entries:
            if not entry:
                continue
            try:
                idx = int(entry)
            except ValueError:
                print(f"Invalid input: {entry} (expected number, 'all', 'list', or 'ok')")
                continue
            if idx < 1 or idx > len(sql_files):
                print(f"Number out of range: {idx}")
                continue
            sql_path = sql_files[idx - 1]
            run_psql(sql_path, args.db_user, args.host, args.port, args.password)
        # re-print list after executing the selected files so user can pick more
        print_sql_list()


if __name__ == '__main__':
    main()
